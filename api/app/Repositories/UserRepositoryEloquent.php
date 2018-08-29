<?php

namespace Backend\Repositories;

use Prettus\Repository\Eloquent\BaseRepository;
use Prettus\Repository\Criteria\RequestCriteria;
use Backend\Repositories\UserRepository;
use Backend\Models\User;
use Backend\Validators\UserValidator;

/**
 * Class UserRepositoryEloquent
 * @package namespace Backend\Repositories;
 */
class UserRepositoryEloquent extends BaseRepository implements UserRepository
{
    /**
     * Save a new entity in repository
     *
     * @param array $attributes
     * @return mixed
     * @throws \Prettus\Validator\Exceptions\ValidatorException
     */
    public function create(array $attributes)
    {
        $attributes['password'] = bcrypt($attributes['password']);
        return parent::create($attributes);
    }


    /**
     * Specify Model class name
     *
     * @return string
     */
    public function model()
    {
        return User::class;
    }


    /**
     * Boot up the repository, pushing criteria
     */
    public function boot()
    {
        $this->pushCriteria(app(RequestCriteria::class));
    }
}
