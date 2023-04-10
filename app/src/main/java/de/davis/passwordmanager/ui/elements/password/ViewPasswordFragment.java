package de.davis.passwordmanager.ui.elements.password;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.textfield.TextInputLayout;

import de.davis.passwordmanager.R;
import de.davis.passwordmanager.databinding.FragmentViewPasswordBinding;
import de.davis.passwordmanager.listeners.OnInformationChangedListener;
import de.davis.passwordmanager.manager.ActivityResultManager;
import de.davis.passwordmanager.security.element.SecureElement;
import de.davis.passwordmanager.security.element.password.PasswordDetails;
import de.davis.passwordmanager.ui.elements.ViewSecureElementFragment;
import de.davis.passwordmanager.ui.views.PasswordStrengthBar;
import de.davis.passwordmanager.utils.BrowserUtil;

public class ViewPasswordFragment extends ViewSecureElementFragment {

    public static final int ID = R.id.viewPasswordFragment;

    private FragmentViewPasswordBinding binding;
    private EditText editText;

    @Override
    public void fillInElement(@NonNull SecureElement password) {
        super.fillInElement(password);

        ActivityResultManager activityResultManager = ActivityResultManager.getOrCreateManager(getClass(), this);
        activityResultManager.registerGeneratePassword(result -> editText.setText(result));

        PasswordDetails details = (PasswordDetails) password.getDetail();

        binding.password.setInformation(details.getPassword());
        binding.password.setOnChangedListener(new OnInformationChangedListener<>(password, (element, changes) -> {
            details.setPassword(changes);
            setStrengthValues(details);
            return details;
        }));
        binding.password.setOnEditDialogViewCreatedListener(view -> {
            editText = ((TextInputLayout) view.findViewById(R.id.textInputLayout)).getEditText();
            PasswordStrengthBar passwordStrengthBar = view.findViewById(R.id.strengthBar);
            passwordStrengthBar.update(editText.getText().toString(), false);
            editText.addTextChangedListener(passwordStrengthBar);

            view.findViewById(R.id.generate).setOnClickListener(v -> activityResultManager.launchGeneratePassword(getContext()));
        });

        binding.origin.setInformation(details.getOrigin());
        binding.origin.setOnChangedListener(new OnInformationChangedListener<>(password, (element, changes) -> {
            details.setOrigin(changes);
            manageOrigin(details);
            return details;
        }));

        binding.username.setInformation(details.getUsername());
        binding.username.setOnChangedListener(new OnInformationChangedListener<>(password, (element, changes) -> {
            details.setUsername(changes);
            return details;
        }));

        setStrengthValues(details);
        manageOrigin(details);
    }

    @Override
    public View getContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(binding == null)
            binding = FragmentViewPasswordBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    private void setStrengthValues(PasswordDetails details){
        binding.strength.setInformation(details.getStrength().getString());
        binding.strength.setInformationTextColor(details.getStrength().getColor(requireContext()));
    }

    private void manageOrigin(PasswordDetails details){
        if(BrowserUtil.isValidURL(BrowserUtil.ensureProtocol(details.getOrigin()))) {
            binding.origin.setOnEndButtonClickListener(v -> BrowserUtil.open(details.getOrigin(), requireContext()));
            binding.origin.setEndButtonDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_open_in_new_24));
        }
    }
}