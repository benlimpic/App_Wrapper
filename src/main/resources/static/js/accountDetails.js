document.addEventListener("DOMContentLoaded", () => {
  //ACCOUNT DETAILS HEADER
  const accountDetailsHeader = document.getElementById("accountDetailsHeader");
  const updateBox = document.getElementById("updateBox");

  // -----------------------------------------------------------------------------

  // UPDATE USERNAME
  const updateUsernameButton = document.getElementById("updateUsernameButton");
  const updateUsernameForm = document.getElementById("updateUsernameForm");
  const cancelUpdateUsernameButton = document.getElementById(
    "cancelUpdateUsernameButton",
  );

  // Show the update username form when the button is clicked
  updateUsernameButton.addEventListener("click", () => {
    accountDetailsHeader.textContent = "Update Username";
    updateBox.classList.remove("hidden");
    updateUsernameForm.classList.remove("hidden");
    updateNameForm.classList.add("hidden");
    updateEmailForm.classList.add("hidden");
    updatePasswordForm.classList.add("hidden");
  });

  // Hide the update username form when the cancel button is clicked
  cancelUpdateUsernameButton.addEventListener("click", () => {
    accountDetailsHeader.textContent = "";
    updateBox.classList.add("hidden");
    updateUsernameForm.classList.add("hidden");
  });

  // -----------------------------------------------------------------------------

  // UPDATE NAME
  const updateNameButton = document.getElementById("updateNameButton");
  const updateNameForm = document.getElementById("updateNameForm");
  const cancelUpdateNameButton = document.getElementById(
    "cancelUpdateNameButton",
  );

  // Show the update username form when the button is clicked

  updateNameButton.addEventListener("click", () => {
    accountDetailsHeader.textContent = "Update Name";
    updateBox.classList.remove("hidden");
    updateNameForm.classList.remove("hidden");

    updateUsernameForm.classList.add("hidden");
    updateEmailForm.classList.add("hidden");
    updatePasswordForm.classList.add("hidden");
  });

  // Hide the update username form when the cancel button is clicked
  cancelUpdateNameButton.addEventListener("click", () => {
    accountDetailsHeader.textContent = "";
    updateBox.classList.add("hidden");
    updateNameForm.classList.add("hidden");
  });

  // -----------------------------------------------------------------------------

  // UPDATE EMAIL
  const updateEmailButton = document.getElementById("updateEmailButton");
  const updateEmailForm = document.getElementById("updateEmailForm");
  const cancelUpdateEmailButton = document.getElementById(
    "cancelUpdateEmailButton",
  );

  // Show the update email form when the button is clicked
  updateEmailButton.addEventListener("click", () => {
    accountDetailsHeader.textContent = "Update Email";
    updateBox.classList.remove("hidden");
    updateEmailForm.classList.remove("hidden");

    updateUsernameForm.classList.add("hidden");
    updateNameForm.classList.add("hidden");
    updatePasswordForm.classList.add("hidden");
  });

  // Hide the update email form when the cancel button is clicked
  cancelUpdateEmailButton.addEventListener("click", () => {
    accountDetailsHeader.textContent = "";
    updateBox.classList.add("hidden");
    updateEmailForm.classList.add("hidden");
  });

  // -----------------------------------------------------------------------------

  // UPDATE PASSWORD
  const updatePasswordButton = document.getElementById("updatePasswordButton");
  const updatePasswordForm = document.getElementById("updatePasswordForm");
  const cancelUpdatePasswordButton = document.getElementById(
    "cancelUpdatePasswordButton",
  );

  // Show the update password form when the button is clicked
  updatePasswordButton.addEventListener("click", () => {
    accountDetailsHeader.textContent = "Update Password";
    updateBox.classList.remove("hidden");
    updatePasswordForm.classList.remove("hidden");

    updateUsernameForm.classList.add("hidden");
    updateNameForm.classList.add("hidden");
    updateEmailForm.classList.add("hidden");
  });

  // Hide the update password form when the cancel button is clicked
  cancelUpdatePasswordButton.addEventListener("click", () => {
    updateBox.classList.add("hidden");
    updatePasswordForm.classList.add("hidden");
  });

  // ---------------------------------------------------------------------------

  // DELETE ACCOUNT
  const deleteAccountForm = document.getElementById("deleteAccountForm");
  const confirmDeleteInput = document.getElementById("confirmDelete");
  const deleteErrorDiv = document.getElementById("deleteError");
  const deleteAccountButton = document.getElementById("deleteAccountButton");
  const cancelDeleteButton = document.getElementById("cancelDeleteButton");
  const confirmDeleteLabel = document.getElementById("confirmDeleteLabel");

  // Show the delete account form when the button is clicked
  deleteAccountButton.addEventListener("click", () => {
    accountDetailsHeader.textContent = "Are You Sure?";
    updateBox.classList.add("hidden");
    deleteAccountForm.classList.remove("hidden");
    deleteAccountButton.classList.add("hidden");
  });

  // Hide the delete account form when the cancel button is clicked
  cancelDeleteButton.addEventListener("click", () => {
    accountDetailsHeader.textContent = "";
    deleteAccountForm.classList.add("hidden");
    deleteAccountButton.classList.remove("hidden");
  });

  // Validate the delete account form when submitted
  deleteAccountForm.addEventListener("submit", (event) => {
    if (confirmDeleteInput.value !== "DELETE") {
      event.preventDefault();
      confirmDeleteInput.classList.add("border-red-500", "ring-red-500");
    } else {
      confirmDeleteInput.classList.remove("border-red-500", "ring-red-500");
    }
  });
});
