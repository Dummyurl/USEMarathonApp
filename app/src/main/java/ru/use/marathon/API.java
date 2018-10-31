package ru.use.marathon;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ilyas on 09-Jun-18.
 */

public interface API {


    @GET("api.php")
    Call<JsonObject> sign_up(
            @Query("v") int v,
            @Query("method") String method,
            @Query("type") int type,
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password,
            @Query("phone") String num,
            @Query("id_region") String id_region,
            @Query("id_city") String id_city,
            @Query("id_subject") int id_subject,
            @Query("style_type") String style_type,
            @Query("vkuser_id") String vkuser_id


    );


    @GET("api.php")
    Call<JsonObject> sign_in(
            @Query("v") int v,
            @Query("method") String method,
            @Query("type") int type,
            @Query("email") String email,
            @Query("password") String password,
            @Query("style_type") String style_type

    );


    @GET("api.php")
    Call<JsonObject> restorePassword(
            @Query("v") int v,
            @Query("method") String method,
            @Query("email") String email,
            @Query("utype") int type,
            @Query("new_password") String password

    );


    @GET("api.php")
    Call<JsonObject> regionshow(
            @Query("v") int v,
            @Query("method") String method,
            @Query("utype") int type

    );
    @GET("api.php")
    Call<JsonObject> cityshow(
            @Query("v") int v,
            @Query("method") String method,
            @Query("id_region") int id_region

    );



    @GET("api.php")
    Call<JsonObject> sendPHPMail(
            @Query("v") int v,
            @Query("method") String method,
            @Query("email") String email


    );


    @GET("api.php")
    Call<JsonObject> get_collections(
            @Query("v") int v,
            @Query("method") String method,
            @Query("sbj") int sbj
    );


    @GET("api.php")
    Call<JsonObject> get_topics(
            @Query("v") int v,
            @Query("method") String method,
            @Query("sbj") int sbj
    );


    @GET("api.php")
    Call<JsonObject> get_solved_tests_by_topic(
            @Query("v") int v,
            @Query("method") String method,
            @Query("sbj") int sbj,
            @Query("user_id") int userid
    );

    @GET("api.php")
    Call<JsonObject> get_collection_by_topics(
            @Query("v") int v,
            @Query("method") String method,
            @Query("topic_id") int topic_ids
    );

    @GET("api.php")
    Call<JsonObject> get_collection(
            @Query("v") int v,
            @Query("method") String method,
            @Query("sbj") int sbj,
            @Query("qc_number") int qc_number

    );


    @GET("api.php")
    Call<JsonObject> get_theory_topics(
            @Query("v") int v,
            @Query("method") String method,
            @Query("sbj") int sbj
    ); // интерфейс формирования запроса, тут ставлю в каком виде принимаю response


    @GET("api.php")
    Call<JsonObject> get_all_feed(
            @Query("v") int v,
            @Query("method") String method,
            @Query("type") int type
    );

    @GET("api.php")
    Call<JsonObject> check_email(
            @Query("v") int v,
            @Query("method") String method,
            @Query("type") int type,
            @Query("email") String email
    );


    @GET("api.php")
    Call<JsonObject> favorites(
            @Query("v") int v,
            @Query("method") String method,
            @Query("id_user") int id_user,
            @Query("id_topic") String id_topic,
            @Query("type") int type
    );


    @GET("api.php")
    Call<JsonObject> getFeedTextById(
            @Query("v") int v,
            @Query("method") String method,
            @Query("id") int id
    );


    @GET("api.php")
    Call<JsonObject> updateRegToken(
            @Query("v") int v,
            @Query("method") String method,
            @Query("reg_token") String token,
            @Query("uid") int user_id,
            @Query("utype") int user_type
    );


    @GET("api.php")
    Call<JsonObject> getChatRooms(
            @Query("v") int v,
            @Query("method") String method,
            @Query("uid") int user_id,
            @Query("utype") int user_type
    );


    @GET("api.php")
    Call<JsonObject> getMessages(
            @Query("v") int v,
            @Query("method") String method,
            @Query("chat_id") String chat_id,
            @Query("uid") String user_id,
            @Query("utype") int user_type
    );


    @GET("api.php")
    Call<JsonObject> sendMsg(
            @Query("v") int v,
            @Query("method") String method,
            @Query("msg") String msg,
            @Query("cht_id") int cht_id,
            @Query("uid") String user_id,
            @Query("utype") int user_type,
            @Query("name") String user_name
    );

    @GET("api.php")
    Call<JsonObject> getAllUsers(
            @Query("v") int v,
            @Query("method") String method,
            @Query("type") int type
    );


    @GET("api.php")
    Call<JsonObject> createChat(
            @Query("v") int v,
            @Query("method") String method,
            @Query("title") String title,
            @Query("uid") String user_id,
            @Query("utype") int user_type
    );


    @GET("api.php")
    Call<JsonObject> leaveChat(
            @Query("v") int v,
            @Query("method") String method,
            @Query("chat_id") String chat_id,
            @Query("uid") String user_id,
            @Query("utype") int user_type
    );

    @GET("api.php")
    Call<JsonObject> addChatMember(
            @Query("v") int v,
            @Query("method") String method,
            @Query("chat_id") String chat_id,
            @Query("uid") String user_id,
            @Query("utype") int user_type
    );

    @GET("api.php")
    Call<JsonObject> getChatMembers(
            @Query("v") int v,
            @Query("method") String method,
            @Query("chat_id") String chat_id
    );


    @GET("api.php")
    Call<JsonObject> sendStat(
            @Query("v") int v,
            @Query("method") String method,
            @Query("sbj") int sbj,
            @Query("uid") int uid,
            @Query("qu_id") int qu_id,
            @Query("time") int time,
            @Query("ra") int ra,
            @Query("t_id") int topic_id
    );


    //CREATE TASK


    @GET("api.php")
    Call<JsonObject> create_qu(
            @Query("v") int v,
            @Query("method") String method,
            @Query("subject") String subject,
            @Query("topic_id") int topic_id,
            @Query("task_num") int task_number,
            @Query("ans_type") int answer_type
    );


    @GET("api.php")
    Call<JsonObject> add_qu_content(
            @Query("v") int v,
            @Query("method") String method,
            @Query("qu_id") int qu_id,
            @Query("text") String content
    );



    @GET("api.php")
    Call<JsonObject> add_qu_ans(
            @Query("v") int v,
            @Query("method") String method,
            @Query("qu_id") int qu_id,
            @Query("text") String content
    );

    @GET("api.php")
    Call<JsonObject> add_qu_right_ans(
            @Query("v") int v,
            @Query("method") String method,
            @Query("qu_id") int qu_id,
            @Query("ans_id") String content
    );


    //TEACHER PROFILE

    @GET("api.php")
    Call<JsonObject> getTeachersStudents(
            @Query("v") int v,
            @Query("method") String method,
            @Query("teacher_id") int teacher_id
    );


    @GET("api.php")
    Call<JsonObject> getUserInfo(
            @Query("v") int v,
            @Query("method") String method,
            @Query("user_id") String user_id,
            @Query("utype") int user_type
    );


    @GET("api.php")
    Call<JsonObject> getStats(
            @Query("v") int v,
            @Query("method") String method,
            @Query("sbj") int sbj,
            @Query("user_id") String user_id
    );

    @GET("api.php")
    Call<JsonObject> setSolvedTopic(
            @Query("v") int v,
            @Query("method") String method,
            @Query("sbj") String sbj,
            @Query("uid") int uid,
            @Query("topic_id") String topic_id
    );


    @GET("api.php")
    Call<JsonObject> getSolvedByTopics(
            @Query("v") int v,
            @Query("method") String method,
            @Query("sbj") int sbj,
            @Query("uid") int uid
    );





    @GET("api.php")
    Call<JsonObject> checkSubscription(
            @Query("v") int v,
            @Query("method") String method,
            @Query("token") String  token
    );



}
