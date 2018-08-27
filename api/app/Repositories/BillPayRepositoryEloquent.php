<?php

namespace Backend\Repositories;

use Prettus\Repository\Eloquent\BaseRepository;
use Prettus\Repository\Criteria\RequestCriteria;
use Backend\Repositories\BillPayRepository;
use Backend\Models\BillPay;
use Backend\Validators\BillPayValidator;

/**
 * Class BillPayRepositoryEloquent
 * @package namespace Backend\Repositories;
 */
class BillPayRepositoryEloquent extends BaseRepository implements BillPayRepository
{
    /**
     * Specify Model class name
     *
     * @return string
     */
    public function model()
    {
        return BillPay::class;
    }

    

    /**
     * Boot up the repository, pushing criteria
     */
    public function boot()
    {
        $this->pushCriteria(app(RequestCriteria::class));
    }
}
