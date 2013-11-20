package fieldType.noLobs.clientDao;

import fieldType.noLobs.modelDto.UserTutorial;
import fieldType.noLobs.modelDto.UserTutorialExample;
import fieldType.noLobs.modelDto.UserTutorialWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserTutorialMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERTUTORIAL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int countByExample(UserTutorialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERTUTORIAL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int deleteByExample(UserTutorialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERTUTORIAL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int insert(UserTutorialWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERTUTORIAL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int insertSelective(UserTutorialWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERTUTORIAL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    List<UserTutorialWithBLOBs> selectByExampleWithBLOBs(UserTutorialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERTUTORIAL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    List<UserTutorial> selectByExample(UserTutorialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERTUTORIAL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByExampleSelective(@Param("record") UserTutorialWithBLOBs record, @Param("example") UserTutorialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERTUTORIAL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByExampleWithBLOBs(@Param("record") UserTutorialWithBLOBs record, @Param("example") UserTutorialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERTUTORIAL
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByExample(@Param("record") UserTutorial record, @Param("example") UserTutorialExample example);
}