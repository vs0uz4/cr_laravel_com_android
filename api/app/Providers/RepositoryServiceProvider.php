<?php

namespace Backend\Providers;

use Illuminate\Support\ServiceProvider;

class RepositoryServiceProvider extends ServiceProvider
{
    /**
     * Bootstrap the application services.
     *
     * @return void
     */
    public function boot()
    {
        //
    }

    /**
     * Register the application services.
     *
     * @return void
     */
    public function register()
    {
        $this->app->bind(\Backend\Repositories\CategoryRepository::class, \Backend\Repositories\CategoryRepositoryEloquent::class);
        $this->app->bind(\Backend\Repositories\BillPayRepository::class, \Backend\Repositories\BillPayRepositoryEloquent::class);
        $this->app->bind(\Backend\Repositories\UserRepository::class, \Backend\Repositories\UserRepositoryEloquent::class);
        //:end-bindings:
    }
}
