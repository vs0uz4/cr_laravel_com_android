<?php

namespace Backend\Repositories;

use Prettus\Repository\Contracts\RepositoryInterface;

/**
 * Interface BillPayRepository
 * @package namespace Backend\Repositories;
 */
interface BillPayRepository extends RepositoryInterface, MultitenancyRepository
{
    public function calculateTotal();
}
