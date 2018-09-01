<?php

namespace Backend\Http\Controllers\Api;

use Illuminate\Http\Request;
use Backend\Http\Controllers\Controller;
use Tymon\JWTAuth\Exceptions\JWTException;

class AuthController extends Controller
{
    /**
     * @SWG\Swagger(
     *     basePath="/api",
     *     schemes={"http"},
     *     consumes={"application/json"},
     *     produces={"application/json"},
     *     @SWG\Info(
     *         version="0.0.1",
     *         title="Backend API",
     *         description="BillPays API RESTful",
     *         @SWG\Contact(
     *              name="Vitor Rodriuues",
     *              email="vitor.rodrigues@gmail.com",
     *              url="https://github.com/vs0uz4/cr_laravel_com_android"
     *         ),
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
     *          description="Return Created JWT Token"
     *     ),
     *     @SWG\Response(
     *          response="401",
     *          description="Invalid Credentials"
     *     ),
     *     @SWG\Response(
     *          response="500",
     *          description="Could not Create Token"
     *     )
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

        return response()->json(compact('token'), 200);
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
