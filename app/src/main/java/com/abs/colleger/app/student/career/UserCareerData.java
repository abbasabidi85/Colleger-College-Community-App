package com.abs.colleger.app.student.career;

public class UserCareerData {
    String jobTitle;
    String jobDescription;
    String jobCompanyName;
    String linkedinId;
    String facebookId;
    String instagramId;
    String image;
    String category;
    String key;

    public UserCareerData(){

    }

    public UserCareerData(String jobTitle, String jobDescription, String jobCompanyName, String linkedinId, String facebookId, String instagramId, String image, String category, String key){
        this.jobTitle=jobTitle;
        this.jobDescription=jobDescription;
        this.jobCompanyName=jobCompanyName;
        this.image=image;
        this.category=category;
        this.key=key;
    }




    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobCompanyName() {
        return jobCompanyName;
    }

    public void setJobCompanyName(String jobCompanyName) {
        this.jobCompanyName = jobCompanyName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
