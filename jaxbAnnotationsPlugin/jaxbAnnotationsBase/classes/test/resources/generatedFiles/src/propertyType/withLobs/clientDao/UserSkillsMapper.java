package propertyType.withLobs.clientDao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import propertyType.withLobs.modelDto.UserSkills;
import propertyType.withLobs.modelDto.UserSkillsExample;

public interface UserSkillsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int countByExample(UserSkillsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int deleteByExample(UserSkillsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int deleteByPrimaryKey(Short skillId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int insert(UserSkills record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int insertSelective(UserSkills record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    List<UserSkills> selectByExample(UserSkillsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    UserSkills selectByPrimaryKey(Short skillId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByExampleSelective(@Param("record") UserSkills record, @Param("example") UserSkillsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByExample(@Param("record") UserSkills record, @Param("example") UserSkillsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByPrimaryKeySelective(UserSkills record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERSKILLS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByPrimaryKey(UserSkills record);
}