<?php

namespace Backend\Presenters;

use Backend\Transformers\BillPayTransformer;
use Prettus\Repository\Presenter\FractalPresenter;

/**
 * Class BillPayPresenter
 *
 * @package namespace Backend\Presenters;
 */
class BillPayPresenter extends FractalPresenter
{
    /**
     * Transformer
     *
     * @return \League\Fractal\TransformerAbstract
     */
    public function getTransformer()
    {
        return new BillPayTransformer();
    }
}
