<?php

/*
|--------------------------------------------------------------------------
| Model Factories
|--------------------------------------------------------------------------
|
| Here you may define all of your model factories. Model factories give
| you a convenient way to create models for testing and seeding your
| database. Just tell the factory how a default model should look.
|
*/

/** @var \Illuminate\Database\Eloquent\Factory $factory */
$factory->define(Backend\Models\User::class, function (Faker\Generator $faker) {
    static $password;

    return [
        'name' => $faker->name,
        'email' => $faker->unique()->safeEmail,
        'password' => $password ?: $password = bcrypt('secret'),
        'remember_token' => str_random(10),
    ];
});

/** @var \Illuminate\Database\Eloquent\Factory $factory */
$factory->define(Backend\Models\Category::class, function (Faker\Generator $faker) {
    return [
        'name'      => $faker->name,
        'user_id'   => rand(1, 21),
    ];
});

/** @var \Illuminate\Database\Eloquent\Factory $factory */
$factory->define(Backend\Models\BillPay::class, function (Faker\Generator $faker) {
    return [
        'name' => $faker->name,
        'date_due' => $faker->date(),
        'value' => $faker->randomFloat(2,100,10000),
        'done' => (bool) rand(0,1),
        'category_id' => rand(1, 50),
        'user_id'   => rand(1, 21),
    ];
});
