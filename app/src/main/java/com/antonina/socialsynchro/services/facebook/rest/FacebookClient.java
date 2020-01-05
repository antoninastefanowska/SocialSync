package com.antonina.socialsynchro.services.facebook.rest;

import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.rest.BaseClient;
import com.antonina.socialsynchro.common.utils.ApplicationConfig;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookCreateContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookCreateContentWithMediaRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookPostRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetPagePictureRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetPageRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetUserPagesRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookInspectTokenRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookRemoveContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookUpdateContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookUploadPhotoRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookUploadVideoFinishRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookUploadVideoStartRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookUploadVideoTransferRequest;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookContentResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookCountResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookIdentifierResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetPagePictureResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetUserPagesResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookInspectTokenResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookPageResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookUploadVideoFinishResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookUploadVideoStartResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookUploadVideoTransferResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class FacebookClient extends BaseClient {
    private static final String BASE_URL = "https://graph.facebook.com/";
    private static final String CALLBACK_URL = "https://socialsynchro.pythonanywhere.com/backend/post_facebook_token";

    private FacebookClient() { }

    public static String getLoginURL(String state) {
        ApplicationConfig config = ApplicationConfig.getInstance();
        String clientID = config.getKey("facebook_app_id");
        String scope = "manage_pages,pages_manage_cta,pages_show_list,publish_pages";
        return "https://www.facebook.com/v5.0/dialog/oauth?client_id=" + clientID + "&redirect_uri=" + CALLBACK_URL + "&state=" + state + "&response_type=token" + "&scope=" + scope;
    }

    public static LiveData<FacebookInspectTokenResponse> inspectToken(FacebookInspectTokenRequest request) {
        InspectTokenController controller = new InspectTokenController(request);
        return controller.start();
    }

    public static LiveData<FacebookGetUserPagesResponse> getUserPages(FacebookGetUserPagesRequest request) {
        GetUserPagesController controller = new GetUserPagesController(request);
        return controller.start();
    }

    public static LiveData<FacebookPageResponse> getPage(FacebookGetPageRequest request) {
        GetPageController controller = new GetPageController(request);
        return controller.start();
    }

    public static LiveData<FacebookGetPagePictureResponse> getPagePicture(FacebookGetPagePictureRequest request) {
        GetPagePictureController controller = new GetPagePictureController(request);
        return controller.start();
    }

    public static LiveData<FacebookIdentifierResponse> createContent(FacebookCreateContentRequest request) {
        CreateContentController controller = new CreateContentController(request);
        return controller.start();
    }

    public static LiveData<FacebookIdentifierResponse> createContentWithMedia(FacebookCreateContentWithMediaRequest request) {
        CreateContentWithMediaController controller = new CreateContentWithMediaController(request);
        return controller.start();
    }

    public static LiveData<FacebookContentResponse> getContent(FacebookPostRequest request) {
        GetContentController controller = new GetContentController(request);
        return controller.start();
    }

    public static LiveData<FacebookIdentifierResponse> updateContent(FacebookUpdateContentRequest request) {
        UpdateContentController controller = new UpdateContentController(request);
        return controller.start();
    }

    public static LiveData<FacebookIdentifierResponse> removeContent(FacebookRemoveContentRequest request) {
        RemoveContentController controller = new RemoveContentController(request);
        return controller.start();
    }

    public static LiveData<FacebookIdentifierResponse> uploadPhoto(FacebookUploadPhotoRequest request) {
        UploadPhotoController controller = new UploadPhotoController(request);
        return controller.start();
    }

    public static LiveData<FacebookUploadVideoStartResponse> uploadVideoStart(FacebookUploadVideoStartRequest request) {
        UploadVideoStartController controller = new UploadVideoStartController(request);
        return controller.start();
    }

    public static LiveData<FacebookUploadVideoTransferResponse> uploadVideoTransfer(FacebookUploadVideoTransferRequest request) {
        UploadVideoTransferController controller = new UploadVideoTransferController(request);
        return controller.start();
    }

    public static LiveData<FacebookUploadVideoFinishResponse> uploadVideoFinish(FacebookUploadVideoFinishRequest request) {
        UploadVideoFinishController controller = new UploadVideoFinishController(request);
        return controller.start();
    }

    public static LiveData<FacebookCountResponse> getPostReactions(FacebookPostRequest request) {
        GetPostReactionsController controller = new GetPostReactionsController(request);
        return controller.start();
    }

    public static LiveData<FacebookCountResponse> getPostComments(FacebookPostRequest request) {
        GetPostCommentsController controller = new GetPostCommentsController(request);
        return controller.start();
    }

    private static class InspectTokenController extends BaseController<FacebookInspectTokenRequest, FacebookInspectTokenResponse> {

        public InspectTokenController(FacebookInspectTokenRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookInspectTokenResponse createResponse() {
            return new FacebookInspectTokenResponse();
        }

        @Override
        protected Call<FacebookInspectTokenResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.inspectToken(request.getInputToken(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookInspectTokenResponse> getResponseClass() {
            return FacebookInspectTokenResponse.class;
        }
    }

    private static class GetUserPagesController extends BaseController<FacebookGetUserPagesRequest, FacebookGetUserPagesResponse> {

        public GetUserPagesController(FacebookGetUserPagesRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookGetUserPagesResponse createResponse() {
            return new FacebookGetUserPagesResponse();
        }

        @Override
        protected Call<FacebookGetUserPagesResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.getUserPages(request.getUserID(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookGetUserPagesResponse> getResponseClass() {
            return FacebookGetUserPagesResponse.class;
        }
    }

    private static class GetPageController extends BaseController<FacebookGetPageRequest, FacebookPageResponse> {

        public GetPageController(FacebookGetPageRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookPageResponse createResponse() {
            return new FacebookPageResponse();
        }

        @Override
        protected Call<FacebookPageResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.getPage(request.getPageID(), request.getFields(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookPageResponse> getResponseClass() {
            return FacebookPageResponse.class;
        }
    }

    private static class GetPagePictureController extends BaseController<FacebookGetPagePictureRequest, FacebookGetPagePictureResponse> {

        public GetPagePictureController(FacebookGetPagePictureRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookGetPagePictureResponse createResponse() {
            return new FacebookGetPagePictureResponse();
        }

        @Override
        protected Call<FacebookGetPagePictureResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.getPagePicture(request.getPageID(), request.getRedirect(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookGetPagePictureResponse> getResponseClass() {
            return FacebookGetPagePictureResponse.class;
        }
    }

    private static class CreateContentController extends BaseController<FacebookCreateContentRequest, FacebookIdentifierResponse> {

        public CreateContentController(FacebookCreateContentRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookIdentifierResponse createResponse() {
            return new FacebookIdentifierResponse();
        }

        @Override
        protected Call<FacebookIdentifierResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.createContent(request.getPageID(), request.getMessage(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookIdentifierResponse> getResponseClass() {
            return FacebookIdentifierResponse.class;
        }
    }

    private static class CreateContentWithMediaController extends BaseController<FacebookCreateContentWithMediaRequest, FacebookIdentifierResponse> {

        public CreateContentWithMediaController(FacebookCreateContentWithMediaRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookIdentifierResponse createResponse() {
            return new FacebookIdentifierResponse();
        }

        @Override
        protected Call<FacebookIdentifierResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.createContentWithMedia(request.getPageID(), request.getMessage(), request.getMediaIDs(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookIdentifierResponse> getResponseClass() {
            return FacebookIdentifierResponse.class;
        }
    }

    private static class GetContentController extends BaseController<FacebookPostRequest, FacebookContentResponse> {

        public GetContentController(FacebookPostRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookContentResponse createResponse() {
            return new FacebookContentResponse();
        }

        @Override
        protected Call<FacebookContentResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.getContent(request.getPostID(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookContentResponse> getResponseClass() {
            return FacebookContentResponse.class;
        }
    }

    private static class UpdateContentController extends BaseController<FacebookUpdateContentRequest, FacebookIdentifierResponse> {

        public UpdateContentController(FacebookUpdateContentRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookIdentifierResponse createResponse() {
            return new FacebookIdentifierResponse();
        }

        @Override
        protected Call<FacebookIdentifierResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.updateContent(request.getPostID(), request.getMessage(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookIdentifierResponse> getResponseClass() {
            return FacebookIdentifierResponse.class;
        }
    }

    private static class RemoveContentController extends BaseController<FacebookRemoveContentRequest, FacebookIdentifierResponse> {

        public RemoveContentController(FacebookRemoveContentRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookIdentifierResponse createResponse() {
            return new FacebookIdentifierResponse();
        }

        @Override
        protected Call<FacebookIdentifierResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.removeContent(request.getPostID(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookIdentifierResponse> getResponseClass() {
            return FacebookIdentifierResponse.class;
        }
    }

    private static class UploadPhotoController extends BaseController<FacebookUploadPhotoRequest, FacebookIdentifierResponse> {

        public UploadPhotoController(FacebookUploadPhotoRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookIdentifierResponse createResponse() {
            return new FacebookIdentifierResponse();
        }

        @Override
        protected Call<FacebookIdentifierResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.uploadPhoto(request.getPageID(), request.getPhoto(), request.getPublished(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookIdentifierResponse> getResponseClass() {
            return FacebookIdentifierResponse.class;
        }
    }

    private static class UploadVideoStartController extends BaseController<FacebookUploadVideoStartRequest, FacebookUploadVideoStartResponse> {

        public UploadVideoStartController(FacebookUploadVideoStartRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookUploadVideoStartResponse createResponse() {
            return new FacebookUploadVideoStartResponse();
        }

        @Override
        protected Call<FacebookUploadVideoStartResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.uploadVideoStart(request.getPageID(), request.getUploadPhase(), request.getFileSize(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookUploadVideoStartResponse> getResponseClass() {
            return FacebookUploadVideoStartResponse.class;
        }
    }

    private static class UploadVideoTransferController extends BaseController<FacebookUploadVideoTransferRequest, FacebookUploadVideoTransferResponse> {

        public UploadVideoTransferController(FacebookUploadVideoTransferRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookUploadVideoTransferResponse createResponse() {
            return new FacebookUploadVideoTransferResponse();
        }

        @Override
        protected Call<FacebookUploadVideoTransferResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.uploadVideoTransfer(request.getPageID(), request.getUploadPhase(), request.getUploadSessionID(), request.getStartOffset(), request.getFileChunk(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookUploadVideoTransferResponse> getResponseClass() {
            return FacebookUploadVideoTransferResponse.class;
        }
    }

    private static class UploadVideoFinishController extends BaseController<FacebookUploadVideoFinishRequest, FacebookUploadVideoFinishResponse> {

        public UploadVideoFinishController(FacebookUploadVideoFinishRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookUploadVideoFinishResponse createResponse() {
            return new FacebookUploadVideoFinishResponse();
        }

        @Override
        protected Call<FacebookUploadVideoFinishResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.uploadVideoFinish(request.getPageID(), request.getUploadPhase(), request.getUploadSessionID(), request.getTitle(), request.getDescription(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookUploadVideoFinishResponse> getResponseClass() {
            return FacebookUploadVideoFinishResponse.class;
        }
    }

    private static class GetPostReactionsController extends BaseController<FacebookPostRequest, FacebookCountResponse> {

        public GetPostReactionsController(FacebookPostRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookCountResponse createResponse() {
            return new FacebookCountResponse();
        }

        @Override
        protected Call<FacebookCountResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.getPostReactions(request.getPostID(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookCountResponse> getResponseClass() {
            return FacebookCountResponse.class;
        }
    }

    private static class GetPostCommentsController extends BaseController<FacebookPostRequest, FacebookCountResponse> {

        public GetPostCommentsController(FacebookPostRequest request) {
            super(request);
        }

        @Override
        protected String getBaseURL() {
            return BASE_URL;
        }

        @Override
        protected FacebookCountResponse createResponse() {
            return new FacebookCountResponse();
        }

        @Override
        protected Call<FacebookCountResponse> createCall() {
            FacebookAPI facebookAPI = retrofit.create(FacebookAPI.class);
            return facebookAPI.getPostComments(request.getPostID(), request.getAuthorizationString());
        }

        @Override
        protected Class<FacebookCountResponse> getResponseClass() {
            return FacebookCountResponse.class;
        }
    }
}
