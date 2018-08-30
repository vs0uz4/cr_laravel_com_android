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
     * Display a listing of the resource.
     *
     * @return mixed
     */
    public function index()
    {
        return $this->repository->all();
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param CategoryRequest $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function store(CategoryRequest $request)
    {
        $category = $this->repository->create($request->all());
        return response()->json($category, 201);
    }


    /**
     * Display the specified resource.
     *
     * @param $id
     * @return mixed
     */
    public function show($id)
    {
        return $this->repository->find($id);
    }

    /**
     * Update the specified resource in storage.
     *
     * @param CategoryRequest $request
     * @param string $id
     * @return \Illuminate\Http\JsonResponse
     */
    public function update(CategoryRequest $request, $id)
    {
        $category = $this->repository->update($request->all(), $id);
        return response()->json($category, 200);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param int $id
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
