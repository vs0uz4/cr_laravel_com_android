<?php

namespace Backend\Repositories;

/**
 * Interface Multitenancy
 * @package namespace Backend\Repositories;
 */
interface MultitenancyRepository
{
    public function applyMultitenancy();
}