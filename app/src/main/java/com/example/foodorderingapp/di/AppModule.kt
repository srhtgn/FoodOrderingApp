package com.example.foodorderingapp.di

import android.content.Context
import androidx.room.Room
import com.example.foodorderingapp.data.datasource.CartDataSource
import com.example.foodorderingapp.data.datasource.FavoritesDataSource
import com.example.foodorderingapp.data.datasource.FoodsDataSource
import com.example.foodorderingapp.data.repository.firebase.AuthRepository
import com.example.foodorderingapp.data.repository.CartRepository
import com.example.foodorderingapp.data.repository.FavoritesRepository
import com.example.foodorderingapp.data.repository.FoodsRepository
import com.example.foodorderingapp.retrofit.ApiUtils
import com.example.foodorderingapp.retrofit.CartDao
import com.example.foodorderingapp.retrofit.FoodsDao
import com.example.foodorderingapp.room.Database
import com.example.foodorderingapp.room.FavoritesDao
import com.example.foodorderingapp.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    //Retrofit
    @Provides
    @Singleton
    fun provideFoodsRepository(fds: FoodsDataSource): FoodsRepository {
        return FoodsRepository(fds)
    }

    @Provides
    @Singleton
    fun provideFoodsDataSource(fDao: FoodsDao): FoodsDataSource {
        return FoodsDataSource(fDao)
    }

    @Provides
    @Singleton
    fun provideFoodsDao(): FoodsDao {
        return ApiUtils.getFoodsDao()
    }

    @Provides
    @Singleton
    fun provideCartRepository(cds: CartDataSource): CartRepository {
        return CartRepository(cds)
    }

    @Provides
    @Singleton
    fun provideCartDataSource(cDao: CartDao): CartDataSource {
        return CartDataSource(cDao)
    }

    @Provides
    @Singleton
    fun provideCartDao(): CartDao {
        return ApiUtils.getCartDao()
    }

    //Room

    @Provides
    @Singleton
    fun provideFavoritesRepository(favDs: FavoritesDataSource): FavoritesRepository {
        return FavoritesRepository(favDs)
    }

    @Provides
    @Singleton
    fun provideFavoritesDataSource(favDao: FavoritesDao): FavoritesDataSource {
        return FavoritesDataSource(favDao)
    }

    @Provides
    @Singleton
    fun provideFavoritesDao(@ApplicationContext context: Context): FavoritesDao {
        val database = Room.databaseBuilder(context, Database::class.java, "favorites.sqlite")
            .createFromAsset("favorites.sqlite").build()
        return database.getFavoritesDao()
    }


    //Firebase

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserViewModel(userRepository: AuthRepository): UserViewModel {
        return UserViewModel(userRepository)
    }

}