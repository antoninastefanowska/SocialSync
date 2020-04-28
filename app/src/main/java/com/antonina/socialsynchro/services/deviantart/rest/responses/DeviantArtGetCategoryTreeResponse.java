package com.antonina.socialsynchro.services.deviantart.rest.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviantArtGetCategoryTreeResponse extends DeviantArtResponse {
    @SerializedName("categories")
    private List<CategoryResponse> categories;

    public List<CategoryResponse> getCategories() {
        return categories;
    }

    public static class CategoryResponse extends DeviantArtResponse {
        @SerializedName("catpath")
        private String catpath;

        @SerializedName("title")
        private String title;

        @SerializedName("has_subcategory")
        private boolean hasSubcategory;

        public String getCatpath() {
            return catpath;
        }

        public String getTitle() {
            return title;
        }

        public boolean getHasSubcategory() {
            return hasSubcategory;
        }
    }
}
