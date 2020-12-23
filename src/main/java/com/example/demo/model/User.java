package com.example.demo.model;

public class User {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.id
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.account_id
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    private String accountId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.name
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.token
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    private String token;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.time_create
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    private Long timeCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.time_modify
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    private Long timeModify;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.avatar_url
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    private String avatarUrl;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.id
     *
     * @return the value of user.id
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.id
     *
     * @param id the value for user.id
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.account_id
     *
     * @return the value of user.account_id
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.account_id
     *
     * @param accountId the value for user.account_id
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.name
     *
     * @return the value of user.name
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.name
     *
     * @param name the value for user.name
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.token
     *
     * @return the value of user.token
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public String getToken() {
        return token;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.token
     *
     * @param token the value for user.token
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.time_create
     *
     * @return the value of user.time_create
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public Long getTimeCreate() {
        return timeCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.time_create
     *
     * @param timeCreate the value for user.time_create
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public void setTimeCreate(Long timeCreate) {
        this.timeCreate = timeCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.time_modify
     *
     * @return the value of user.time_modify
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public Long getTimeModify() {
        return timeModify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.time_modify
     *
     * @param timeModify the value for user.time_modify
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public void setTimeModify(Long timeModify) {
        this.timeModify = timeModify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.avatar_url
     *
     * @return the value of user.avatar_url
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.avatar_url
     *
     * @param avatarUrl the value for user.avatar_url
     *
     * @mbg.generated Wed Dec 23 17:14:37 CST 2020
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl == null ? null : avatarUrl.trim();
    }
}