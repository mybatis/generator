package propertyType.withLobs.modelDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name="userSkills")
public class UserSkills {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERSKILLS.SKILL_ID
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private Short skillId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERSKILLS.SKILL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private String skill;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USERSKILLS.IS_ACTIVE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    private Boolean isActive;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERSKILLS.SKILL_ID
     *
     * @return the value of USERSKILLS.SKILL_ID
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public Short getSkillId() {
        return skillId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERSKILLS.SKILL_ID
     *
     * @param skillId the value for USERSKILLS.SKILL_ID
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setSkillId(Short skillId) {
        this.skillId = skillId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERSKILLS.SKILL
     *
     * @return the value of USERSKILLS.SKILL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public String getSkill() {
        return skill;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERSKILLS.SKILL
     *
     * @param skill the value for USERSKILLS.SKILL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setSkill(String skill) {
        this.skill = skill;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USERSKILLS.IS_ACTIVE
     *
     * @return the value of USERSKILLS.IS_ACTIVE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USERSKILLS.IS_ACTIVE
     *
     * @param isActive the value for USERSKILLS.IS_ACTIVE
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    public UserSkills() {
        //Needed For JAXB serialization.
    }
}