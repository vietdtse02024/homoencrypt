package example.com.homomorphiccrypto.Model;

import java.io.Serializable;

public class Person implements Serializable {
    private int id;
    private String name;
    private String address;
    private String description;
    private String imgUrl;
    private String voteCount;
    private Boolean isSelected = false;

    public Person() {
    }

    public Person(int id, String name, String address, String imgUrl, String voteCount) {
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
        this.setImgUrl(imgUrl);
        this.setVoteCount(voteCount);
        this.setSelected(false);
    }

    @Override
    public String toString() {
        return getId() + ":" + getSelected();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
