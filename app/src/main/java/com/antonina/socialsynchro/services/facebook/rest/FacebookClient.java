package com.antonina.socialsynchro.services.facebook.rest;

import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.rest.BaseClient;
import com.antonina.socialsynchro.common.utils.ApplicationConfig;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookCreateContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookCreateContentWithMediaRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetPagePictureRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetPageRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetUserPagesRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookInspectTokenRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookRemoveContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookUpdateContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookUploadPhotoRequest;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookContentResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookIdentifierResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetPagePictureResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetUserPagesResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookInspectTokenResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookPageResponse;

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

    public static LiveData<FacebookContentResponse> getContent(FacebookGetContentRequest request) {
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
            return facebookAPI.inspectToken(request.getInputToken(), request.getAuthorizationHeader());
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
            return facebookAPI.getUserPages(request.getUserID(), request.getAuthorizationHeader());
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
            return facebookAPI.getPage(request.getPageID(), request.getAuthorizationHeader());
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
            return facebookAPI.getPagePicture(request.getPageID(), request.getRedirect(), request.getAuthorizationHeader());
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
            return facebookAPI.createContent(request.getPageID(), request.getMessage(), request.getAuthorizationHeader());
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
            return facebookAPI.createContentWithMedia(request.getPageID(), request.getMessage(), request.getMediaIDs(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<FacebookIdentifierResponse> getResponseClass() {
            return FacebookIdentifierResponse.class;
        }
    }

    private static class GetContentController extends BaseController<FacebookGetContentRequest, FacebookContentResponse> {

        public GetContentController(FacebookGetContentRequest request) {
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
            return facebookAPI.getContent(request.getPostID(), request.getAuthorizationHeader());
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
            return facebookAPI.updateContent(request.getPostID(), request.getMessage(), request.getAuthorizationHeader());
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
            return facebookAPI.removeContent(request.getPostID(), request.getAuthorizationHeader());
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
            return facebookAPI.uploadPhoto(request.getPageID(), request.getPhoto(), request.getPublished(), request.getAuthorizationHeader());
        }

        @Override
        protected Class<FacebookIdentifierResponse> getResponseClass() {
            return FacebookIdentifierResponse.class;
        }
    }
}
