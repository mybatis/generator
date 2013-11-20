package propertyType.withLobs.modelDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name="userTutorial")
public class UserTutorial {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERTUTORIAL.USER_ID
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERTUTORIAL.TITLE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private String title;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERTUTORIAL.VIDEO_TYPE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private String videoType;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERTUTORIAL.USER_ID
     *
     * @return the value of USERTUTORIAL.USER_ID
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERTUTORIAL.USER_ID
     *
     * @param userId the value for USERTUTORIAL.USER_ID
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERTUTORIAL.TITLE
     *
     * @return the value of USERTUTORIAL.TITLE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERTUTORIAL.TITLE
     *
     * @param title the value for USERTUTORIAL.TITLE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERTUTORIAL.VIDEO_TYPE
     *
     * @return the value of USERTUTORIAL.VIDEO_TYPE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public String getVideoType() {
        return videoType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERTUTORIAL.VIDEO_TYPE
     *
     * @param videoType the value for USERTUTORIAL.VIDEO_TYPE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERTUTORIAL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public UserTutorial() {
        //Needed For JAXB serialization.
    }
}