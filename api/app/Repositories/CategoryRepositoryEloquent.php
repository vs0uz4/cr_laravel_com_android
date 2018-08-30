<?php

namespace Backend\Repositories;

use Backend\Presenters\CategoryPresenter;
use Prettus\Repository\Eloquent\BaseRepository;
use Prettus\Repository\Criteria\RequestCriteria;
use Backend\Models\Category;

/**
 * Class CategoryRepositoryEloquent
 * @package namespace Backend\Repositories;
 */
class CategoryRepositoryEloquent extends BaseRepository implements CategoryRepository
{
    /**
     * Specify Model class name
     *
     * @return string
     */
    public function model()
    {
        return Category::class;
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
        return CategoryPresenter::class;
    }

    /**
     * Applying method applyMultitenancy for Clear Booted Category Model
     */
    public function applyMultitenancy()
    {
        Category::clearBootedModels();
    }
}
