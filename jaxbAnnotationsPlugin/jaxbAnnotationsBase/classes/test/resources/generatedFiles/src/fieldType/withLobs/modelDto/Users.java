package fieldType.withLobs.modelDto;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="usERs")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
public class Users {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERS.USER_ID
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    @XmlAttribute
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERS.LOGIN
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private String login;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERS.FIRST_NAME
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    @XmlAttribute(name="first_name",required=true)
    private String firstName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERS.LAST_NAME
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private String lastName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERS.PASSWORD
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private String password;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERS.IS_ACTIVE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private Boolean isActive;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERS.CREATE_DATE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private Date createDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERS.USER_ID
     *
     * @return the value of USERS.USER_ID
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERS.USER_ID
     *
     * @param userId the value for USERS.USER_ID
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERS.LOGIN
     *
     * @return the value of USERS.LOGIN
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public String getLogin() {
        return login;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERS.LOGIN
     *
     * @param login the value for USERS.LOGIN
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERS.FIRST_NAME
     *
     * @return the value of USERS.FIRST_NAME
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERS.FIRST_NAME
     *
     * @param firstName the value for USERS.FIRST_NAME
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERS.LAST_NAME
     *
     * @return the value of USERS.LAST_NAME
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERS.LAST_NAME
     *
     * @param lastName the value for USERS.LAST_NAME
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERS.PASSWORD
     *
     * @return the value of USERS.PASSWORD
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERS.PASSWORD
     *
     * @param password the value for USERS.PASSWORD
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERS.IS_ACTIVE
     *
     * @return the value of USERS.IS_ACTIVE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERS.IS_ACTIVE
     *
     * @param isActive the value for USERS.IS_ACTIVE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERS.CREATE_DATE
     *
     * @return the value of USERS.CREATE_DATE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERS.CREATE_DATE
     *
     * @param createDate the value for USERS.CREATE_DATE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public Users() {
        //Needed For JAXB serialization.
    }
}