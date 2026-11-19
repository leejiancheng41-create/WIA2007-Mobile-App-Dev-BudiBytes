package com.um.rainguard.ui.base;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.um.rainguard.R;
import com.um.rainguard.data.repository.ReportRepository;
import com.um.rainguard.data.model.CampusZones;
import com.um.rainguard.data.model.User;
import com.um.rainguard.databinding.BottomSheetPreferencesBinding;
import com.um.rainguard.databinding.DialogNavigationMenuBinding;
import com.um.rainguard.databinding.PopupProfileBinding;
import com.um.rainguard.ui.auth.MainActivity;
import com.um.rainguard.utils.Constants;
import com.um.rainguard.utils.NavigationHelper;
import com.um.rainguard.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class BaseActivity extends ComponentActivity {
    protected SharedPreferences preferences;
    protected ReportRepository repository;

    protected String role;
    protected String userEmail;
    /** users.userId for the signed-in account; reports.reporterId is compared against it. */
    protected String userId;
    /** The users row of the signed-in account, once Room has delivered it. */
    protected User currentUser;
    protected String currentPage = "dashboard";
    protected String returnPage = "dashboard";
    protected String selectedReportId;

    private final List<Runnable> observerDetach = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackNavigation();
            }
        });

        getWindow().setStatusBarColor(Constants.BG);
        getWindow().setNavigationBarColor(Constants.BG);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        preferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);
        repository = ReportRepository.getInstance(this);

        role = getIntent().getStringExtra(Constants.EXTRA_ROLE);
        userEmail = getIntent().getStringExtra(Constants.EXTRA_EMAIL);

        if (role == null && preferences.getBoolean("remember", false)) {
            String savedRole = preferences.getString("role", null);
            String savedEmail = preferences.getString("email", null);
            if (("admin".equals(savedRole) || "student".equals(savedRole))
                    && savedEmail != null
                    && android.util.Patterns.EMAIL_ADDRESS.matcher(savedEmail).matches()) {
                role = savedRole;
                userEmail = savedEmail;
            }
        }

        if (role == null || userEmail == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Identity comes from the signed-in e-mail for now; the users table is the record of it.
        userId = ReportRepository.userIdForEmail(userEmail);
        repository.ensureUser(userEmail, role);
        observe(repository.observeUser(userId), user -> currentUser = user);

        currentPage = getIntent().getStringExtra(Constants.EXTRA_PAGE);
        if (currentPage == null) currentPage = getInitialPage();
        selectedReportId = getIntent().getStringExtra(Constants.EXTRA_REPORT_ID);
        returnPage = getIntent().getStringExtra(Constants.EXTRA_RETURN_PAGE);
        if (returnPage == null) returnPage = "dashboard";

        setContentView(getLayoutResourceId());

        View rootContent = findViewById(android.R.id.content);
        if (rootContent != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootContent, (v, insets) -> {
                Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
                v.setPadding(0, statusBarInsets.top, 0, 0);
                return insets;
            });
        }

        setupHeaderAndNavigation();
        initView();
    }

    protected abstract int getLayoutResourceId();

    protected abstract String getInitialPage();

    protected abstract void initView();

    protected boolean isAdmin() {
        return "admin".equals(role);
    }

    protected void setupHeaderAndNavigation() {
        TextView breadcrumb = findViewById(R.id.workspace_breadcrumb);
        if (breadcrumb != null) {
            breadcrumb.setText("Universiti Malaya / " + (isAdmin() ? "Operations" : "Campus safety"));
        }

        TextView title = findViewById(R.id.workspace_title);
        if (title != null) {
            title.setText(NavigationHelper.getPageTitle(currentPage, isAdmin()));
        }

        View menuBtn = findViewById(R.id.workspace_menu);
        if (menuBtn != null) {
            menuBtn.setOnClickListener(v -> showNavigationMenu());
        }

        View notifBtn = findViewById(R.id.workspace_notifications);
        if (notifBtn != null) {
            notifBtn.setOnClickListener(v -> navigate("notifications"));
            GradientDrawable notifBg = new GradientDrawable();
            notifBg.setColor(Constants.WHITE);
            notifBg.setCornerRadius(UIUtils.dp(this, 8));
            notifBg.setStroke(UIUtils.dp(this, 1), Color.rgb(226, 232, 240));
            notifBtn.setBackground(notifBg);
        }

        TextView roleBadge = findViewById(R.id.workspace_profile);
        if (roleBadge != null) {
            roleBadge.setText(isAdmin() ? "🛡 Admin" : "🛡 Campus User");
            GradientDrawable badgeBg = new GradientDrawable();
            badgeBg.setColor(isAdmin() ? Color.rgb(254, 243, 199) : Color.rgb(239, 246, 255));
            badgeBg.setCornerRadius(UIUtils.dp(this, 12));
            badgeBg.setStroke(UIUtils.dp(this, 1), isAdmin() ? Color.rgb(253, 230, 138) : Color.rgb(191, 219, 254));
            roleBadge.setBackground(badgeBg);
            roleBadge.setTextColor(isAdmin() ? Color.rgb(180, 83, 9) : Color.rgb(29, 78, 216));
            roleBadge.setOnClickListener(v -> showProfileBubble(v));
        }

        TextView mockBadge = findViewById(R.id.mock_data_badge);
        if (mockBadge != null) {
            GradientDrawable mockBg = new GradientDrawable();
            mockBg.setColor(Color.rgb(255, 247, 237));
            mockBg.setCornerRadius(UIUtils.dp(this, 12));
            mockBg.setStroke(UIUtils.dp(this, 1), Color.rgb(254, 215, 170));
            mockBadge.setBackground(mockBg);
        }

        setupBottomNavigation();
    }

    protected void setupBottomNavigation() {
        LinearLayout bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav == null) return;

        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.removeAllViews();

        List<String[]> navItems = NavigationHelper.getNavigationItems(isAdmin());
        int visibleCount = Math.min(4, navItems.size());
        for (int i = 0; i < visibleCount; i++) {
            String pageKey = navItems.get(i)[0];
            String label = navItems.get(i)[1];
            addNavItem(bottomNav, pageKey, label);
        }
        addNavItem(bottomNav, "more", "More");
    }

    private void addNavItem(LinearLayout parent, String pageKey, String label) {
        TextView item = new TextView(this);
        item.setText(NavigationHelper.getNavGlyph(pageKey) + "\n" + label);
        item.setGravity(Gravity.CENTER);
        item.setTextSize(10);
        item.setLineSpacing(0, 0.9f);
        item.setTextColor(currentPage.equals(pageKey) ? Constants.NAVY : Constants.MUTED);
        item.setPadding(UIUtils.dp(this, 2), UIUtils.dp(this, 4), UIUtils.dp(this, 2), UIUtils.dp(this, 4));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        parent.addView(item, params);

        item.setOnClickListener(v -> {
            if ("more".equals(pageKey)) {
                showNavigationMenu();
            } else {
                navigate(pageKey);
            }
        });
    }

    public void navigate(String target) {
        boolean allowed = "notifications".equals(target);
        for (String[] item : NavigationHelper.getNavigationItems(isAdmin())) {
            if (item[0].equals(target)) {
                allowed = true;
                break;
            }
        }
        if (!allowed) {
            showToast("This page is not available for your role.");
            target = "dashboard";
        }
        NavigationHelper.navigateTo(this, role, userEmail, target, null, target);
        finish();
    }

    protected void showNavigationMenu() {
        DialogNavigationMenuBinding binding = DialogNavigationMenuBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(binding.getRoot())
                .create();

        binding.navDialogTitle.setText(isAdmin() ? "Admin navigation" : "Campus user navigation");
        binding.navDialogSubtitle.setText("UM RainGuard · " + (isAdmin() ? "Admin" : "Campus User"));
        binding.btnCloseNavDialog.setOnClickListener(v -> dialog.dismiss());

        List<String[]> nav = NavigationHelper.getNavigationItems(isAdmin());
        for (String[] item : nav) {
            String pageKey = item[0];
            String label = item[1];

            TextView navRow = new TextView(this);
            navRow.setText(NavigationHelper.getNavGlyph(pageKey) + "   " + label);
            navRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            navRow.setTextColor(Color.rgb(15, 23, 42));
            navRow.setPadding(UIUtils.dp(this, 12), UIUtils.dp(this, 12), UIUtils.dp(this, 12), UIUtils.dp(this, 12));
            navRow.setBackground(UIUtils.createButtonDrawable(this, Constants.WHITE, Color.rgb(241, 245, 249), 6));

            navRow.setOnClickListener(v -> {
                dialog.dismiss();
                navigate(pageKey);
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = UIUtils.dp(this, 4);
            binding.navDialogItemsContainer.addView(navRow, params);
        }

        binding.btnNavSignOut.setOnClickListener(v -> {
            dialog.dismiss();
            signOut();
        });

        dialog.show();
    }

    protected void showProfileBubble(View anchorView) {
        if (anchorView == null) return;

        PopupProfileBinding binding = PopupProfileBinding.inflate(getLayoutInflater());
        binding.popupUserEmail.setText(userEmail);
        binding.popupUserRole.setText("Current role: " + (isAdmin() ? "Admin" : "Campus User"));

        PopupWindow popup = new PopupWindow(binding.getRoot(), UIUtils.dp(this, 240), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setElevation(UIUtils.dp(this, 8));

        binding.btnPopupSignOut.setOnClickListener(v -> {
            popup.dismiss();
            signOut();
        });

        popup.showAsDropDown(anchorView, -UIUtils.dp(this, 120), UIUtils.dp(this, 4));
    }

    public void showPreferencesBottomSheet() {
        BottomSheetDialog sheet = new BottomSheetDialog(this);
        BottomSheetPreferencesBinding binding = BottomSheetPreferencesBinding.inflate(getLayoutInflater());

        binding.btnClosePreferences.setOnClickListener(v -> sheet.dismiss());

        // Preferences live on the users row, not in a JSON blob keyed by e-mail.
        List<String> selectedZoneList = new ArrayList<>();
        if (currentUser != null) {
            binding.switchHeavyRain.setChecked(currentUser.notifyHeavyRain);
            binding.switchFlashFlood.setChecked(currentUser.notifyFlashFlood);
            binding.switchWeeklySummary.setChecked(currentUser.weeklySummary);
            selectedZoneList.addAll(currentUser.zones());
        } else {
            binding.switchHeavyRain.setChecked(true);
            binding.switchFlashFlood.setChecked(true);
            binding.switchWeeklySummary.setChecked(false);
            selectedZoneList.add("KK8");
        }

        bindZonePills(binding, selectedZoneList);

        binding.btnSavePreferences.setOnClickListener(v -> {
            repository.savePreferences(userId,
                    binding.switchHeavyRain.isChecked(),
                    binding.switchFlashFlood.isChecked(),
                    binding.switchWeeklySummary.isChecked(),
                    selectedZoneList);
            sheet.dismiss();
            showToast("Preferences saved.");
        });

        sheet.setContentView(binding.getRoot());
        sheet.show();
    }

    private void bindZonePills(BottomSheetPreferencesBinding binding, List<String> selectedZoneList) {
        LinearLayout zoneContainer = binding.zonesFlexContainer;
        zoneContainer.removeAllViews();

        LinearLayout currentRow = new LinearLayout(this);
        currentRow.setOrientation(LinearLayout.HORIZONTAL);
        int currentLineWidth = 0;
        int maxLineWidthDp = 280;

        for (String zone : CampusZones.ALL) {
            TextView zonePill = new TextView(this);
            zonePill.setText(zone);
            zonePill.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            zonePill.setPadding(UIUtils.dp(this, 12), UIUtils.dp(this, 6), UIUtils.dp(this, 12), UIUtils.dp(this, 6));

            boolean isSelected = selectedZoneList.contains(zone);
            updateZonePillStyle(zonePill, isSelected);

            zonePill.setOnClickListener(v -> {
                if (selectedZoneList.contains(zone)) {
                    selectedZoneList.remove(zone);
                    updateZonePillStyle(zonePill, false);
                } else {
                    selectedZoneList.add(zone);
                    updateZonePillStyle(zonePill, true);
                }
            });

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.rightMargin = UIUtils.dp(this, 6);
            p.bottomMargin = UIUtils.dp(this, 6);

            int pillApproxDp = zone.length() * 8 + 24;
            if (currentLineWidth + pillApproxDp > maxLineWidthDp) {
                zoneContainer.addView(currentRow, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                currentRow = new LinearLayout(this);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                currentLineWidth = 0;
            }
            currentRow.addView(zonePill, p);
            currentLineWidth += pillApproxDp;
        }
        zoneContainer.addView(currentRow, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void updateZonePillStyle(TextView pill, boolean selected) {
        if (selected) {
            pill.setBackground(UIUtils.createButtonDrawable(this,
                    Color.rgb(27, 73, 101), Color.rgb(17, 50, 71), 16));
            pill.setTextColor(Constants.WHITE);
        } else {
            pill.setBackground(UIUtils.createBorderButtonDrawable(this,
                    Constants.WHITE, Color.rgb(241, 245, 249), Color.rgb(226, 232, 240), 16));
            pill.setTextColor(Color.rgb(15, 23, 42));
        }
    }

    protected void signOut() {
        preferences.edit().clear().apply();
        role = null;
        userEmail = null;
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Observers use observeForever and are detached in onDestroy to avoid callbacks after
     * this Activity is destroyed.
     */
    protected <T> void observe(LiveData<T> liveData, Observer<T> observer) {
        liveData.observeForever(observer);
        observerDetach.add(() -> liveData.removeObserver(observer));
    }

    @Override
    protected void onDestroy() {
        for (Runnable detach : observerDetach) {
            detach.run();
        }
        observerDetach.clear();
        super.onDestroy();
    }

    private void handleBackNavigation() {
        if (role != null && "report-details".equals(currentPage)) {
            NavigationHelper.navigateTo(this, role, userEmail, returnPage, null, returnPage);
            finish();
        } else if (role != null && !"dashboard".equals(currentPage)) {
            NavigationHelper.navigateTo(this, role, userEmail, "dashboard", null, "dashboard");
            finish();
        } else if (role != null) {
            signOut();
        } else {
            finish();
        }
    }
}
