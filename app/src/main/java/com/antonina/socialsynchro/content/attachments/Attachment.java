package com.antonina.socialsynchro.content.attachments;

import android.databinding.Bindable;

import com.antonina.socialsynchro.content.Post;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.repositories.AttachmentRepository;
import com.antonina.socialsynchro.database.tables.AttachmentTable;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.gui.GUIItem;
import com.antonina.socialsynchro.services.IResponse;
import com.antonina.socialsynchro.services.IServiceEntity;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.RequestBody;

@SuppressWarnings("WeakerAccess")
public abstract class Attachment extends GUIItem implements IDatabaseEntity, IServiceEntity {
    private Long internalID;
    private String externalID;
    private File file;
    private AttachmentType attachmentType;
    private Post parentPost;
    private boolean loading;

    public Attachment() { }

    public Attachment(File file) {
        this.file = file;
    }

    @Override
    public Long getInternalID() { return internalID; }

    @Override
    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    @Bindable
    public File getFile() { return file; }

    @Bindable
    public long getSizeBytes() {
        return file.length();
    }

    public AttachmentType getAttachmentType() { return attachmentType; }

    public void setAttachmentType(AttachmentType attachmentType) { this.attachmentType = attachmentType; }

    public Post getParentPost() { return parentPost; }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;
    }

    public Attachment(IDatabaseTable data) {
        createFromData(data);
    }

    @Override
    public void createFromData(IDatabaseTable data) {
        AttachmentTable attachmentData = (AttachmentTable)data;
        this.internalID = attachmentData.id;
        this.externalID = attachmentData.externalID;
        this.file = new File(attachmentData.filepath);
        this.attachmentType = AttachmentTypes.getAttachmentType(attachmentData.attachmentTypeID);
    }

    @Override
    public void createFromResponse(IResponse response) {
        // TODO
    }

    @Override
    public void saveInDatabase() {
        AttachmentRepository repository = AttachmentRepository.getInstance();
        internalID = repository.insert(this);
    }

    @Override
    public void updateInDatabase() {
        AttachmentRepository repository = AttachmentRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        AttachmentRepository repository = AttachmentRepository.getInstance();
        repository.delete(this);
        internalID = null;
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public String getFileExtension() {
        String filename = file.getName();
        int index = filename.lastIndexOf(".");
        if (index != -1 && index != 0)
            return filename.substring(index + 1);
        else
            return "";
    }

    public String getMIMEType() {
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    private byte[] getFileChunk(long chunkStart, long chunkEnd) {
        byte[] output = null;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            int chunkSize = (int)(chunkEnd - chunkStart + 1);
            output = new byte[chunkSize];
            randomAccessFile.seek(chunkStart);
            randomAccessFile.read(output);
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public RequestBody getChunkRequestBody(long chunkStart, long chunkEnd) {
        return RequestBody.create(MediaType.parse(getMIMEType()), getFileChunk(chunkStart, chunkEnd));
    }
}
