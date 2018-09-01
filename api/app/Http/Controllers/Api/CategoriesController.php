<?php

namespace Backend\Http\Controllers\Api;

use Backend\Http\Controllers\Controller;
use Backend\Http\Requests\CategoryRequest;
use Backend\Repositories\CategoryRepository;

class CategoriesController extends Controller
{

    /**
     * @var CategoryRepository
     */
    protected $repository;

    public function __construct(CategoryRepository $repository)
    {
        $this->repository = $repository;
        $this->repository->applyMultitenancy();
    }

    /**
     * @SWG\Get(
     *     path="/categories",
     *     operationId="index",
     *     tags={"Categories"},
     *     summary="Display a Listing of the Categories",
     *     description="Display a Listing of the Categories",
     *     @SWG\Parameter(
     *          name="Authorization",
     *          in="header",
     *          required=true,
     *          type="string",
     *          description="Bearer __token__"
     *     ),
     *     @SWG\Response(
     *          response=200,
     *          description="Return a Collection of Categories"
     *     )
     * )
     *
     * Display a listing of the resource.
     *
     * @return mixed
     */
    public function index()
    {
        return $this->repository->all();
    }

    /**
     * @SWG\Post(
     *     path="/categories",
     *     operationId="store",
     *     tags={"Categories"},
     *     summary="Create an Category",
     *     description="Create an Category",
     *     @SWG\Parameter(
     *          name="Authorization",
     *          in="header",
     *          required=true,
     *          type="string",
     *          description="Bearer __token__"
     *     ),
     *     @SWG\Parameter(
     *          name="body",
     *          in="body",
     *          required=true,
     *          @SWG\Schema(
     *              @SWG\Property( property="name", type="string" ),
     *          )
     *     ),
     *     @SWG\Response(
     *          response="201",
     *          description="Return Created Category"
     *     ),
     *     @SWG\Response(
     *          response="422",
     *          description="Unprocessable Entity"
     *     )
     * )
     *
     * Store a newly created resource in storage.
     *
     * @param CategoryRequest $request
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function store(CategoryRequest $request)
    {
        $category = $this->repository->create($request->all());
        return response()->json($category, 201);
    }

    /**
     * @SWG\Get(
     *     path="/categories/{id}",
     *     operationId="show",
     *     tags={"Categories"},
     *     summary="Display an Category",
     *     description="Display an Category",
     *     @SWG\Parameter(
     *          name="Authorization",
     *          in="header",
     *          required=true,
     *          type="string",
     *          description="Bearer __token__"
     *     ),
     *     @SWG\Parameter(
     *          name="id",
     *          in="path",
     *          required=true,
     *          type="integer"
     *     ),
     *     @SWG\Response(
     *          response=200,
     *          description="Return the Found Category"
     *     ),
     *     @SWG\Response(
     *          response=404,
     *          description="Resource not found"
     *     )
     * )
     *
     * Display the specified resource.
     *
     * @param $id
     *
     * @return mixed
     */
    public function show($id)
    {
        return $this->repository->find($id);
    }

    /**
     * @SWG\Put(
     *     path="/categories/{id}",
     *     operationId="update",
     *     tags={"Categories"},
     *     summary="Update Category",
     *     description="Update Category",
     *     @SWG\Parameter(
     *          name="Authorization",
     *          in="header",
     *          required=true,
     *          type="string",
     *          description="Bearer __token__"
     *     ),
     *     @SWG\Parameter(
     *          name="id",
     *          in="path",
     *          required=true,
     *          type="integer"
     *     ),
     *     @SWG\Parameter(
     *          name="body",
     *          in="body",
     *          required=true,
     *          @SWG\Schema(
     *              @SWG\Property( property="name", type="string" ),
     *          )
     *     ),
     *     @SWG\Response(
     *          response="200",
     *          description="Return the Updated Category"
     *     ),
     *     @SWG\Response(
     *          response="404",
     *          description="Resource not Found"
     *     ),
     *     @SWG\Response(
     *          response="422",
     *          description="Unprocessable Entity"
     *     )
     * )
     *
     * Update the specified resource in storage.
     *
     * @param CategoryRequest $request
     * @param string $id
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function update(CategoryRequest $request, $id)
    {
        $category = $this->repository->update($request->all(), $id);
        return response()->json($category, 200);
    }

    /**
     * @SWG\Delete(
     *      path="/categories/{id}",
     *      operationId="destroy",
     *      tags={"Categories"},
     *      summary="Delete an Category",
     *      description="Delete an Category",
     *     @SWG\Parameter(
     *          name="Authorization",
     *          in="header",
     *          required=true,
     *          type="string",
     *          description="Bearer __token__"
     *     ),
     *     @SWG\Parameter(
     *          name="id",
     *          in="path",
     *          required=true,
     *          type="integer"
     *     ),
     *     @SWG\Response(
     *          response=204,
     *          description="No content"
     *     ),
     *     @SWG\Response(
     *          response=500,
     *          description="Resource can not be deleted"
     *     ),
     *     @SWG\Response(
     *          response=404,
     *          description="Resource not Found"
     *     ),
     * )
     *
     * Remove the specified resource from storage.
     *
     * @param int $id
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function destroy($id)
    {
        $deleted = $this->repository->delete($id);

        if ($deleted){
            return response()->json([], 204);
        } else {
            return response()->json(['error' => 'resource_can_not_be_deleted'], 500);
        }
    }
}
