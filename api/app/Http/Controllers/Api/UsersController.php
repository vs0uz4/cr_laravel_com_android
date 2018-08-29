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
     * Store a newly created resource in storage.
     *
     * @param UserRequest $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function store(UserRequest $request){
        $user = $this->repository->create($request->all());
        return response()->json($user, 201);
    }
}
