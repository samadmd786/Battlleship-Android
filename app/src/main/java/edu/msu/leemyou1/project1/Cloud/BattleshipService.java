package edu.msu.leemyou1.project1.Cloud;

import edu.msu.leemyou1.project1.ActiveGames;
import edu.msu.leemyou1.project1.Cloud.Models.ActiveGameResult;
import edu.msu.leemyou1.project1.Cloud.Models.RegisterResult;
import edu.msu.leemyou1.project1.Cloud.Models.LoginResult;
import edu.msu.leemyou1.project1.Cloud.Models.GameResult;
import edu.msu.leemyou1.project1.Cloud.Models.TurnResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


import static edu.msu.leemyou1.project1.Cloud.Cloud.CATALOG_PATH;
import static edu.msu.leemyou1.project1.Cloud.Cloud.GAME_PATH;
import static edu.msu.leemyou1.project1.Cloud.Cloud.REGISTER_PATH;
import static edu.msu.leemyou1.project1.Cloud.Cloud.LOGIN_PATH;

import static edu.msu.leemyou1.project1.Cloud.Cloud.ACTIVE_GAMES_PATH;
import static edu.msu.leemyou1.project1.Cloud.Cloud.TURN_PATH;

public interface BattleshipService {

    @GET(REGISTER_PATH)
    Call<RegisterResult> Register(
            @Query("username") String userId,
            @Query("password") String password
    );

    @GET(CATALOG_PATH)
    Call<ActiveGames> getCatalog(
            @Query("user") String userId,
            @Query("pw") String password
    );

    @GET(LOGIN_PATH)
    Call<LoginResult> Login(
            @Query("username") String userId,
            @Query("password") String password
    );

    @GET(GAME_PATH)
    Call<GameResult> Game();

    @GET(TURN_PATH)
    Call<TurnResult> turnresult();

    @GET(ACTIVE_GAMES_PATH)
    Call<ActiveGameResult> ActiveGameResult();

}
