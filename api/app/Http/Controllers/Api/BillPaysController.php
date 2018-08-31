<?php

namespace Backend\Http\Controllers\Api;

use Backend\Http\Controllers\Controller;
use Backend\Http\Requests\BillPayRequest;
use Backend\Repositories\BillPayRepository;


class BillPaysController extends Controller
{

    /**
     * @var BillPayRepository
     */
    protected $repository;

    public function __construct(BillPayRepository $repository)
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
     * @param BillPayRequest $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function store(BillPayRequest $request)
    {
        $data = $request->except('done');
        $billPay = $this->repository->create($data);
        return response()->json($billPay, 201);
    }

    /**
     * Display the specified resource.
     *
     * @param int $id
     * @return mixed
     */
    public function show($id)
    {
        return $this->repository->find($id);
    }

    /**
     * Update the specified resource in storage.
     *
     * @param BillPayRequest $request
     * @param string $id
     * @return \Illuminate\Http\JsonResponse
     */
    public function update(BillPayRequest $request, $id)
    {
        $billPay = $this->repository->update($request->all(), $id);
        return response()->json($billPay, 200);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int $id
     *
     * @return \Illuminate\Http\Response
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

    public function calculateTotal(){
        return $this->repository->calculateTotal();
    }
}
