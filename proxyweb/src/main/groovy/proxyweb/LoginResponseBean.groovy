package proxyweb

/**
 * Created by jorge on 31/10/16.
 */
class LoginResponseBean {

    String username
    String access_token
    String refresh_token
    List<String>roles
    String token_type
    int expires_in
}
