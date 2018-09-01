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
     * @SWG\Get(
     *     path="/bill_pays",
     *     operationId="index",
     *     tags={"BillPays"},
     *     summary="Display a Listing of the BillPays",
     *     description="Display a Listing of the BillPays",
     *     @SWG\Parameter(
     *          name="Authorization",
     *          in="header",
     *          required=true,
     *          type="string",
     *          description="Bearer __token__"
     *     ),
     *     @SWG\Response(
     *          response=200,
     *          description="Return a Collection of BillPays"
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
     *     path="/bill_pays",
     *     operationId="store",
     *     tags={"BillPays"},
     *     summary="Create an BillPay",
     *     description="Create an BillPay",
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
     *              @SWG\Property( property="date_due", type="string", format="date"),
     *              @SWG\Property( property="value", type="number"),
     *              @SWG\Property( property="category_id", type="integer"),
     *          )
     *     ),
     *     @SWG\Response(
     *          response="201",
     *          description="Return Created BillPay"
     *     ),
     *     @SWG\Response(
     *          response="422",
     *          description="Unprocessable Entity"
     *     )
     * )
     *
     * Store a newly created resource in storage.
     *
     * @param BillPayRequest $request
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function store(BillPayRequest $request)
    {
        $data = $request->except('done');
        $billPay = $this->repository->create($data);
        return response()->json($billPay, 201);
    }

    /**
     * @SWG\Get(
     *     path="/bill_pays/{id}",
     *     operationId="show",
     *     tags={"BillPays"},
     *     summary="Display an BillPay",
     *     description="Display an BillPay",
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
     *          description="Return the Found BillPay"
     *     ),
     *     @SWG\Response(
     *          response=404,
     *          description="Resource not found"
     *     )
     * )
     *
     * Display the specified resource.
     *
     * @param int $id
     *
     * @return mixed
     */
    public function show($id)
    {
        return $this->repository->find($id);
    }

    /**
     * @SWG\Put(
     *     path="/bill_pays/{id}",
     *     operationId="update",
     *     tags={"BillPays"},
     *     summary="Update BillPay",
     *     description="Update BillPay",
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
     *              @SWG\Property( property="date_due", type="string", format="date"),
     *              @SWG\Property( property="value", type="number"),
     *              @SWG\Property( property="category_id", type="integer"),
     *              @SWG\Property( property="done", type="boolean"),
     *          )
     *     ),
     *     @SWG\Response(
     *          response="200",
     *          description="Return the Updated BillPay"
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
     * @param BillPayRequest $request
     * @param string $id
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function update(BillPayRequest $request, $id)
    {
        $billPay = $this->repository->update($request->all(), $id);
        return response()->json($billPay, 200);
    }

    /**
     * @SWG\Delete(
     *      path="/bill_pays/{id}",
     *      operationId="destroy",
     *      tags={"BillPays"},
     *      summary="Delete an BillPay",
     *      description="Delete an BillPay",
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
     * @param  int $id
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

    /**
     * @SWG\Get(
     *      path="/bill_pays/total",
     *      operationId="calculateTotal",
     *      tags={"BillPays"},
     *      summary="Calculate totals and counts of BillPays",
     *      description="Calculate totals and counts of BillPays",
     *     @SWG\Parameter(
     *          name="Authorization",
     *          in="header",
     *          required=true,
     *          type="string",
     *          description="Bearer __token__"
     *     ),
     *     @SWG\Response(
     *          response=200,
     *          description="Return array of Totals and Counts of BillPays"
     *     ),
     * )
     *
     * Calculate totals and counts of BillPays
     *
     * @return array
     */
    public function calculateTotal(){
        return $this->repository->calculateTotal();
    }
}
