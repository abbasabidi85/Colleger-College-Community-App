package com.abs.colleger.app.admin.club;

public class ClubData {

    String clubName, clubDescription, clubFacebookId,clubInstagramId, image, category, key;

    public ClubData(){

    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public String getClubFacebookId() {
        return clubFacebookId;
    }

    public void setClubFacebookId(String clubFacebookId) {
        this.clubFacebookId = clubFacebookId;
    }

    public String getClubInstagramId() {
        return clubInstagramId;
    }

    public void setClubInstagramId(String clubInstagramId) {
        this.clubInstagramId = clubInstagramId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ClubData(String clubName, String clubDescription, String clubFacebookId, String clubInstagramId, String image, String category, String key) {
        this.clubName = clubName;
        this.clubDescription = clubDescription;
        this.image = image;
        this.category = category;
        this.key = key;
    }
}
