package propertyType.noLobs.clientDao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import propertyType.noLobs.modelDto.Users;
import propertyType.noLobs.modelDto.UsersExample;

public interface UsersMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int countByExample(UsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int deleteByExample(UsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int deleteByPrimaryKey(Integer userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int insert(Users record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int insertSelective(Users record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    List<Users> selectByExample(UsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    Users selectByPrimaryKey(Integer userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByExampleSelective(@Param("record") Users record, @Param("example") UsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByExample(@Param("record") Users record, @Param("example") UsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByPrimaryKeySelective(Users record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USERS
     *
     * @mbggenerated Sun Sep 15 15:13:25 HST 2013
     */
    int updateByPrimaryKey(Users record);
}