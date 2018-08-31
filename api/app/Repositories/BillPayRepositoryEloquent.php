<?php

namespace Backend\Repositories;

use Backend\Presenters\BillPayPresenter;
use Illuminate\Support\Collection;
use Prettus\Repository\Eloquent\BaseRepository;
use Prettus\Repository\Criteria\RequestCriteria;
use Backend\Models\BillPay;

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

    /**
     * Specify Presenter class name
     *
     * @return string
     */
    public function presenter()
    {
        return BillPayPresenter::class;
    }

    /**
     * Applying method applyMultitenancy for Clear Booted BillPay Model
     */
    public function applyMultitenancy()
    {
        BillPay::clearBootedModels();
    }

    public function calculateTotal()
    {
        $result = [
            'count'         => 0,
            'count_paid'    => 0,
            'total_paid'    => 0,
            'total_be_paid' => 0
        ];

        /** @var Collection $billPays */
        $billPays = $this->skipPresenter()->all();
        $result['count'] = $billPays->count();
        foreach ($billPays as $billPay){
            $done = (bool) $billPay->done;
            if ($done){
                $value = (float) $billPay->value;
                $result['count_paid']++;
                $result['total_paid'] += $value;
            } else {
                $value = (float) $billPay->value;
                $result['total_be_paid'] += $value;
            }
        }

        return $result;
    }
}
