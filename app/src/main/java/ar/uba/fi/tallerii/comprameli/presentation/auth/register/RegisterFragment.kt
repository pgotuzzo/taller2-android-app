package ar.uba.fi.tallerii.comprameli.presentation.auth.register

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.data.session.FirebaseCredentials
import ar.uba.fi.tallerii.comprameli.domain.profile.Profile
import ar.uba.fi.tallerii.comprameli.presentation.auth.AuthEventsHandler
import ar.uba.fi.tallerii.comprameli.presentation.auth.register.di.RegisterModule
import ar.uba.fi.tallerii.comprameli.presentation.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.auth_register_fragment.*
import timber.log.Timber
import javax.inject.Inject


class RegisterFragment : BaseFragment(), RegisterContract.View {

    companion object {
        fun getInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }

    @Inject
    lateinit var mPresenter: RegisterContract.Presenter

    private lateinit var userDbReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var credentials: FirebaseCredentials
    private var isTraditionalRegister = true
    private var mAuthEventsHandler: AuthEventsHandler? = null
    private val mComponent by lazy { app()!!.component.plus(RegisterModule()) }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mAuthEventsHandler = context as AuthEventsHandler
        } catch (e: Exception) {
            Timber.e(e, "RegisterFragment must implement AuthEventsHandler interface")
        }
    }

    fun setFirebaseCredentials(credentials: FirebaseCredentials) {
        this.isTraditionalRegister = false
        this.credentials = credentials
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mComponent.inject(this)

        auth = FirebaseAuth.getInstance()
        userDbReference = FirebaseDatabase.getInstance().reference.child("User")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.auth_register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this)

        if (isTraditionalRegister) { // Traditional Register (email & password credentials)
            showSimpleRegisterFields()

            createBtn.setOnClickListener {
                createFirebaseUser()
            }

        } else { // Facebook Login / Register
            showFacebookRegisterFields()
            createBtn.setOnClickListener {
                val profile = setUpProfileData(credentials.userId, credentials.userName)
                mPresenter.registerUser(profile)
            }
        }
    }

    private fun showSimpleRegisterFields() {
        userInputEdit.setText("")
        passInput.visibility = View.VISIBLE
        passConfirmInput.visibility = View.VISIBLE
    }

    private fun showFacebookRegisterFields() {
        userInputEdit.setText(credentials.userName)
        passInput.visibility = View.GONE
        passConfirmInput.visibility = View.GONE
    }

    private fun setUpProfileData(userId: String, facebook: String): Profile {
        return Profile(
                email = userInputEdit.text.toString(),
                name = nameInputEdit.text.toString(),
                surname = lastNameInputEdit.text.toString(),
                facebook = facebook,
                avatar = null,
                userId = userId,
                google = null
        )
    }

    private fun createFirebaseUser() {
        val name: String = nameInputEdit.text.toString()
        val lastName: String = lastNameInputEdit.text.toString()
        val email: String = userInputEdit.text.toString()
        val password: String = passInputEdit.text.toString()

        if (isValid()) {

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isComplete) {
                            val user: FirebaseUser? = auth.currentUser

                            val userDB = userDbReference.child(user?.uid.toString())

                            userDB.child("Name").setValue(name)
                            userDB.child("lastName").setValue(lastName)

                            val profile = setUpProfileData(user?.uid.toString(), email)
                            mPresenter.registerUser(profile)
                        }
                    }
        }

    }

    private fun isValid(): Boolean {
        val name: String = nameInputEdit.text.toString()
        val lastName: String = lastNameInputEdit.text.toString()
        val email: String = userInputEdit.text.toString()
        val password: String = passInputEdit.text.toString()
        val confirmPassword: String = passConfirmInputEdit.text.toString()

        var isValid = true

        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            showWriteAlertMessage(R.string.auth_register_error_message_uncomplete_data)
            isValid =  false
        } else if (password != confirmPassword) {
            showWriteAlertMessage(R.string.auth_register_error_message_confirm_password)
            isValid = false
        } else if (password.length < 6) {
            showWriteAlertMessage(R.string.auth_register_error_message_password_length)
            isValid = false
        }

        return isValid
    }

    private fun showWriteAlertMessage(message: Int) {
        AlertDialog.Builder(context)
                .setTitle(R.string.auth_register_error_title)
                .setMessage(message)
                .create()
                .show()
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    override fun showLoginErrorMessage() {
        AlertDialog.Builder(context)
                .setTitle("Ups!")
                .setMessage("Ocurrio un error al intentar iniciar la sesion..\n Intenta logearte desde la pantalla de logeo.")
                .create()
                .show()
    }

    override fun showRegisterErrorMessage() {
        AlertDialog.Builder(context)
                .setTitle("Ups!")
                .setMessage("Ocurrio un error al intentar registrar tu usuario..\n Intenta nuevamente")
                .create()
                .show()
    }

    override fun notifyUserSigned() {
        mAuthEventsHandler?.onAuthComplete()
    }

    override fun doLogin() {
        if (isTraditionalRegister)
            mPresenter.doLogin(userInputEdit.text.toString(), passInputEdit.text.toString())
        else
            mPresenter.doLogin(credentials)
    }

}