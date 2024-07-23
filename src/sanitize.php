<?php
/**
 * Sanitize and validate search term to prevent XSS and SQL injection attacks
 *
 * @param string $searchTerm The input search term from the user
 * @return string The sanitized and validated search term
 */
function sanitizeSearchTerm($searchTerm) {
    // Trim whitespace from the beginning and end of the input
    $searchTerm = trim($searchTerm);
    
    // Remove any HTML tags
    $searchTerm = strip_tags($searchTerm);
    
    // Convert special characters to HTML entities to prevent XSS
    $searchTerm = htmlspecialchars($searchTerm, ENT_QUOTES, 'UTF-8');
    
    // Optionally, you can further validate the input length
    if (strlen($searchTerm) > 255) {
        // If input is too long, truncate it or return an error message
        $searchTerm = substr($searchTerm, 0, 255);
    }

    // Check if the search term contains any prohibited characters or SQL injection patterns
    // For example, you might want to allow only alphanumeric characters and spaces
    // Here we also check for common SQL injection patterns
    if (!preg_match('/^[a-zA-Z0-9\s]+$/', $searchTerm) || 
        preg_match('/(\b(select|union|insert|update|delete|drop|alter|create|truncate|replace|rename|exec)\b|--|#|\/\*|\*\/)/i', $searchTerm)) {
        // If invalid characters or SQL injection patterns are found, return an error message
        return "Invalid search term.";
    }
    
    return $searchTerm;
}
?>
