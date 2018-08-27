<?php

namespace Backend\Transformers;

use League\Fractal\TransformerAbstract;
use Backend\Models\BillPay;

/**
 * Class BillPayTransformer
 * @package namespace Backend\Transformers;
 */
class BillPayTransformer extends TransformerAbstract
{

    /**
     * Transform the \BillPay entity
     * @param \BillPay $model
     *
     * @return array
     */
    public function transform(BillPay $model)
    {
        return [
            'id'         => (int) $model->id,
            'name'       => $model->name,
            'date_due'   => $model->date_due,
            'value'      => $model->value,
            'done'       => (int) $model->done,
            'created_at' => $model->created_at,
            'updated_at' => $model->updated_at
        ];
    }
}
