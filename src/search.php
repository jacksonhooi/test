<?php
require 'sanitize.php';

if ($_SERVER["REQUEST_METHOD"] == "GET" && isset($_GET['query'])) {
    $rawSearchTerm = $_GET['query'];
    $sanitizedSearchTerm = sanitizeSearchTerm($rawSearchTerm);

    if ($sanitizedSearchTerm === "Invalid search term.") {
        // Redirect back to the home page with an error message
        header("Location: index.php?error=Invalid search term. Please try again.");
        exit;
    } else {
        // Redirect to the results page with the sanitized search term
        header("Location: results.php?query=" . urlencode($sanitizedSearchTerm));
        exit;
    }
}
?>
