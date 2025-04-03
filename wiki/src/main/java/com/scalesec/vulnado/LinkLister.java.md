# Documentation: `LinkLister.java`

## Overview
The `LinkLister` class provides functionality to extract hyperlinks from a given URL. It uses the `Jsoup` library for HTML parsing and includes methods to retrieve links while ensuring security by validating the URL's host.

---

## Class: `LinkLister`

### Purpose
The class is designed to fetch all hyperlinks (`<a>` tags) from a webpage and return them as a list of absolute URLs. Additionally, it includes a method to validate the URL to prevent access to private IP addresses.

---

## Methods

### 1. `getLinks(String url)`
#### Description
Fetches all hyperlinks from the given URL and returns them as a list of absolute URLs.

#### Parameters
| Name | Type   | Description                     |
|------|--------|---------------------------------|
| `url` | `String` | The URL of the webpage to parse. |

#### Returns
| Type          | Description                          |
|---------------|--------------------------------------|
| `List<String>` | A list of absolute URLs extracted from the webpage. |

#### Exceptions
| Type          | Description                          |
|---------------|--------------------------------------|
| `IOException` | Thrown if there is an issue connecting to the URL or parsing the webpage. |

#### Logic
1. Connects to the given URL using `Jsoup.connect(url).get()`.
2. Selects all `<a>` elements using `doc.select("a")`.
3. Extracts the absolute URL of each hyperlink using `link.absUrl("href")`.
4. Adds the URLs to a list and returns it.

---

### 2. `getLinksV2(String url)`
#### Description
Fetches hyperlinks from the given URL while ensuring the URL does not point to a private IP address.

#### Parameters
| Name | Type   | Description                     |
|------|--------|---------------------------------|
| `url` | `String` | The URL of the webpage to parse. |

#### Returns
| Type          | Description                          |
|---------------|--------------------------------------|
| `List<String>` | A list of absolute URLs extracted from the webpage. |

#### Exceptions
| Type          | Description                          |
|---------------|--------------------------------------|
| `BadRequest`  | Thrown if the URL points to a private IP address or if any other error occurs during processing. |

#### Logic
1. Parses the URL using `new URL(url)` to extract the host.
2. Checks if the host starts with private IP prefixes (`172.`, `192.168`, or `10.`).
   - If true, throws a `BadRequest` exception with the message "Use of Private IP".
3. If the host is valid, calls the `getLinks(url)` method to fetch the links.
4. Catches any exceptions and wraps them in a `BadRequest` exception.

---

## Insights

### Security Considerations
- **Private IP Validation**: The `getLinksV2` method ensures that URLs pointing to private IP addresses are not processed. This is critical for preventing potential security vulnerabilities such as SSRF (Server-Side Request Forgery).
- **Exception Handling**: The method wraps exceptions in a custom `BadRequest` exception, providing a clear error message for invalid inputs or processing errors.

### Dependencies
- **Jsoup Library**: Used for HTML parsing and extracting hyperlinks. It simplifies the process of working with HTML documents.
- **Java Networking (`java.net`)**: Used for URL validation and host extraction.

### Potential Enhancements
- **Customizable Host Validation**: Allow users to specify additional host validation rules or IP ranges.
- **Logging**: Add logging for better traceability of errors and processing steps.
- **Timeout Handling**: Implement connection timeouts for `Jsoup.connect()` to avoid indefinite waits.

---

## Metadata
| Key         | Value              |
|-------------|--------------------|
| File Name   | `LinkLister.java`  |
| Package     | `com.scalesec.vulnado` |
