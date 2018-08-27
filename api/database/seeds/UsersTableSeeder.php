<?php

use Illuminate\Database\Seeder;

class UsersTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        // Create Administrator User
        factory(\Backend\User::class, 1)->create([
            'email' => 'admin@backend.com.br'
        ]);

        // Create Fake Users
        factory(\Backend\User::class, 20)->create();
    }
}
