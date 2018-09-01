<?php

namespace Backend\Http\Controllers\Api;

use Backend\Http\Requests\UserRequest;
use Backend\Http\Controllers\Controller;
use Backend\Repositories\UserRepository;

class UsersController extends Controller
{
    /**
     * @var UserRepository
     */
    private $repository;

    public function __construct(UserRepository $repository){
        $this->repository = $repository;
    }

    /**
     * @SWG\Post(
     *     path="/users",
     *     operationId="store",
     *     tags={"Users"},
     *     summary="Create an User",
     *     description="Create an User",
     *     @SWG\Parameter(
     *          name="body",
     *          in="body",
     *          required=true,
     *          @SWG\Schema(
     *              @SWG\Property( property="name", type="string" ),
     *              @SWG\Property( property="email", type="string" ),
     *              @SWG\Property( property="password", type="string"),
     *          )
     *     ),
     *     @SWG\Response(
     *          response="201",
     *          description="Return Created User"
     *     ),
     *     @SWG\Response(
     *          response="422",
     *          description="Unprocessable Entity"
     *     )
     * )
     *
     * Store a newly created resource in storage.
     *
     * @param  UserRequest $request
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function store(UserRequest $request){
        $user = $this->repository->create($request->all());
        return response()->json($user, 201);
    }
}
