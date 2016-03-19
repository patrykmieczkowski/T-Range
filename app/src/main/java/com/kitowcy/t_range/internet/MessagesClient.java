package com.kitowcy.t_range.internet;

import retrofit.client.Response;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by Łukasz Marczak
 *
 * @since 19.03.16
 */
public interface MessagesClient {

    String endpoint = "http://adres.com";

    @Multipart
    @POST("/")
    rx.Observable<Response> postAudioMessage(@Part("file") TypedFile file,
                                             @Part("name") String fileName);
}
