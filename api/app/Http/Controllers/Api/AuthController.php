<?php

namespace Backend\Http\Controllers\Api;

use Illuminate\Http\Request;
use Backend\Http\Controllers\Controller;
use Tymon\JWTAuth\Exceptions\JWTException;

class AuthController extends Controller
{
    /**
     * @SWG\Swagger(
     *     swagger="2.0",
     *     basePath="/api",
     *     schemes={"http"},
     *     consumes={"application/json"},
     *     produces={"application/json"},
     *     @SWG\Info(
     *         version="0.0.1",
     *         title="Backend API",
     *         description="BillPays API RESTful",
     *         @SWG\Contact(
     *              name="Vitor Rodrigues",
     *              email="vitor.rodrigues@gmail.com",
     *              url="https://github.com/vs0uz4/cr_laravel_com_android"
     *         ),
     *     ),
     *     @SWG\Definition(
     *         definition="User",
     *         required={"id", "name", "email", "created_at", "updated_at"},
     *         @SWG\Property(
     *             property="id",
     *             type="integer",
     *             format="int64"
     *         ),
     *         @SWG\Property(
     *             property="name",
     *             type="string"
     *         ),
     *         @SWG\Property(
     *             property="email",
     *             type="string"
     *         ),
     *         @SWG\Property(
     *             property="created_at",
     *             type="string",
     *             format="date-time"
     *         ),
     *         @SWG\Property(
     *             property="updated_at",
     *             type="string",
     *             format="date-time"
     *         )
     *     )
     * )
     */

    /**
     * @SWG\Post(
     *     path="/login",
     *     operationId="login",
     *     tags={"Authentication"},
     *     summary="Request an JWT Token",
     *     description="Request an JWT Token",
     *     @SWG\Parameter(
     *          name="body",
     *          in="body",
     *          required=true,
     *          @SWG\Schema(
     *              @SWG\Property( property="email", type="string" ),
     *              @SWG\Property( property="password", type="string" ),
     *          )
     *     ),
     *     @SWG\Response(
     *          response="200",
     *          description="Return Token and User",
     *          @SWG\Schema(
     *              required={"token","user"},
     *              @SWG\Property( property="token", type="string", example="Bearer _token_"),
     *              @SWG\Property( property="user", type="object", ref="#/definitions/User"),
     *          ),
     *     ),
     *     @SWG\Response(
     *          response="401",
     *          description="Invalid Credentials",
     *     ),
     *     @SWG\Response(
     *          response="500",
     *          description="Could not Create Token",
     *     ),
     * )
     *
     * Request a JWT Token
     *
     * @param Request $request
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function login(Request $request)
    {
        $credentials = $request->only('email', 'password');

        try {
            $token = \JWTAuth::attempt($credentials);
        } catch (JWTException $ex) {
            return response()->json(['error' => 'could_not_create_token'], 500);
        }

        if (!$token) {
            return response()->json(['error' => 'invalid_credentials'], 401);
        }

        $user = \JWTAuth::toUser($token);

        return response()->json(compact(['token', 'user']), 200);
    }

    /**
     * @SWG\Post(
     *     path="/logout",
     *     operationId="logout",
     *     tags={"Authentication"},
     *     summary="Revoke a JWT Token",
     *     description="Revoke a JWT Token",
     *     @SWG\Parameter(
     *          name="Authorization",
     *          in="header",
     *          required=true,
     *          type="string",
     *          description="Bearer __token__"
     *     ),
     *     @SWG\Response(
     *          response="204",
     *          description="No Content"
     *     ),
     *     @SWG\Response(
     *          response="400",
     *          description="Bad Request - Invalid Token"
     *     ),
     *     @SWG\Response(
     *          response="401",
     *          description="Unauthorized - Invalid Token"
     *     ),
     *     @SWG\Response(
     *          response="500",
     *          description="Could not Invalidate Token"
     *     )
     * )
     *
     * Revoke JWT Token
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function logout()
    {
        try {
            \JWTAuth::invalidate();
        } catch (JWTException $ex) {
            return response()->json(['error' => 'could_not_invalidate_token'], 500);
        }

        return response()->json([], 204);
    }

    /**
     * @SWG\Post(
     *     path="/refresh_token",
     *     operationId="refresh_token",
     *     tags={"Authentication"},
     *     summary="Refresh a JWT Token",
     *     description="Refresh a JWT Token",
     *     @SWG\Parameter(
     *          name="Authorization",
     *          in="header",
     *          required=true,
     *          type="string",
     *          description="Bearer __token__"
     *     ),
     *     @SWG\Response(
     *          response="200",
     *          description="Return a new JWT Token"
     *     ),
     *     @SWG\Response(
     *          response="500",
     *          description="Could not Refresh Token"
     *     )
     * )
     *
     * Refresh JWT Token
     *
     * @param Request $request
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function refreshToken(Request $request)
    {
        try {
            $bearerToken = \JWTAuth::setRequest($request)->getToken();
            $token = \JWTAuth::refresh($bearerToken);
        } catch (JWTException $ex) {
            return response()->json(['error' => 'could_not_refresh_token'], 500);
        }

        return response()->json(compact('token'), 200);
    }
}
