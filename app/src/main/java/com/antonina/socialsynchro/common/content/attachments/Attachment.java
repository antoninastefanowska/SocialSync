package com.antonina.socialsynchro.common.content.attachments;

import android.databinding.Bindable;

import com.antonina.socialsynchro.common.content.posts.Post;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.repositories.AttachmentRepository;
import com.antonina.socialsynchro.common.database.rows.AttachmentRow;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.GUIItem;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.common.content.services.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLConnection;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressWarnings("WeakerAccess")
public abstract class Attachment extends GUIItem implements IDatabaseEntity, IServiceEntity {
    private Long internalID;
    private String externalID;
    private File file;
    private AttachmentType attachmentType;
    private Post parentPost;

    private int uploadProgress;

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
        notifyGUI();
    }

    public Attachment(IDatabaseRow data) {
        createFromDatabaseRow(data);
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        AttachmentRow attachmentData = (AttachmentRow)data;
        this.internalID = attachmentData.getID();
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
        if (internalID != null)
            updateInDatabase();
        else {
            AttachmentRepository repository = AttachmentRepository.getInstance();
            internalID = repository.insert(this);
        }
    }

    @Override
    public void updateInDatabase() {
        AttachmentRepository repository = AttachmentRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        if (internalID == null)
            return;
        AttachmentRepository repository = AttachmentRepository.getInstance();
        repository.delete(this);
        internalID = null;
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

    public MultipartBody.Part getPart() {
        RequestBody requestBody = RequestBody.create(MediaType.parse(getMIMEType()), file);
        return MultipartBody.Part.createFormData("source", file.getName(), requestBody);
    }

    @Override
    public Service getService() { return null; }

    @Override
    public void synchronize(OnSynchronizedListener listener) { }

    @Override
    public Date getSynchronizationDate() { return null; }

    @Bindable
    public int getUploadProgress() {
        return uploadProgress;
    }

    public void setUploadProgress(int uploadProgress) {
        this.uploadProgress = uploadProgress;
    }

    public Attachment createCopy() {
        Attachment copy = AttachmentFactory.getInstance().create(attachmentType.getID());
        copy.internalID = internalID;
        copy.externalID = externalID;
        copy.file = file;
        copy.attachmentType = attachmentType;
        return copy;
    }
}
