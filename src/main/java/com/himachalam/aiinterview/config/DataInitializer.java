package com.himachalam.aiinterview.config;

import com.himachalam.aiinterview.model.Domain;
import com.himachalam.aiinterview.model.Question;
import com.himachalam.aiinterview.model.User;
import com.himachalam.aiinterview.repository.DomainRepository;
import com.himachalam.aiinterview.repository.QuestionRepository;
import com.himachalam.aiinterview.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Seeds the database with:
 *  - Admin user (admin@aiinterview.com / admin123)
 *  - 7 interview domains
 *  - 84 sample questions (12 per domain)
 * Runs only when the tables are empty — safe to restart without duplicates.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepo;
    private final DomainRepository domainRepo;
    private final QuestionRepository questionRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepo,
                           DomainRepository domainRepo,
                           QuestionRepository questionRepo,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.domainRepo = domainRepo;
        this.questionRepo = questionRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        if (domainRepo.count() == 0) seedDomains();
        if (questionRepo.count() == 0) seedQuestions();
    }

    // ─────────────────────────────────────────────────────────────────────────

    private void seedAdmin() {
        if (userRepo.findByEmail("admin@aiinterview.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@aiinterview.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepo.save(admin);
            log.info("✅ Admin user created: admin@aiinterview.com / admin123");
        }
    }

    private void seedDomains() {
        List<Domain> domains = List.of(
            domain("Java", "Core Java, OOP, Collections, Multithreading, Java 8+", "bi-cup-hot"),
            domain("DSA", "Data Structures & Algorithms — Arrays, Trees, Graphs, Sorting", "bi-diagram-3"),
            domain("DBMS", "Database Management, SQL, Normalization, Transactions", "bi-database"),
            domain("Operating Systems", "Processes, Threads, Memory Management, Scheduling", "bi-cpu"),
            domain("Computer Networks", "OSI Model, TCP/IP, HTTP, DNS, Security", "bi-globe"),
            domain("Spring Boot", "Spring Boot, Spring Security, Spring Data JPA, REST APIs", "bi-gear"),
            domain("HR Interview", "Behavioral questions, communication, career goals", "bi-person-workspace")
        );
        domainRepo.saveAll(domains);
        log.info("✅ {} domains seeded.", domains.size());
    }

    private Domain domain(String name, String desc, String icon) {
        Domain d = new Domain();
        d.setName(name);
        d.setDescription(desc);
        d.setIcon(icon);
        return d;
    }

    private void seedQuestions() {
        Map<String, Domain> domainMap = new LinkedHashMap<>();
        domainRepo.findAll().forEach(d -> domainMap.put(d.getName(), d));

        // ── Java ──────────────────────────────────────────────────────────────
        Domain java = domainMap.get("Java");
        addQ(java, "What is the difference between JDK, JRE, and JVM?",
             "JDK (Java Development Kit) includes JRE + compiler + tools for development. JRE (Java Runtime Environment) includes JVM + standard libraries to run Java apps. JVM (Java Virtual Machine) is the runtime engine that executes bytecode, provides platform independence.", "EASY");
        addQ(java, "Explain the 4 pillars of Object-Oriented Programming.",
             "Encapsulation: bundling data and methods in a class, hiding internal state. Inheritance: child class acquires properties/methods of parent. Polymorphism: one interface, multiple implementations (overloading & overriding). Abstraction: hiding implementation details, exposing only essential features.", "EASY");
        addQ(java, "What is the difference between == and .equals() in Java?",
             "== compares object references (memory addresses). .equals() compares object content/value. For String, == may return false for two objects with same content but .equals() returns true. Always use .equals() for object value comparison.", "EASY");
        addQ(java, "What are Java Collections? Explain the main hierarchy.",
             "Collection is the root interface. List (ArrayList, LinkedList) — ordered, duplicates allowed. Set (HashSet, TreeSet) — unordered, no duplicates. Queue (LinkedList, PriorityQueue) — FIFO. Map (HashMap, TreeMap, LinkedHashMap) — key-value pairs, not part of Collection interface.", "MEDIUM");
        addQ(java, "What is multithreading? How do you create threads in Java?",
             "Multithreading allows concurrent execution of two or more threads. Create threads by: 1) Extending Thread class and overriding run(). 2) Implementing Runnable interface. 3) Implementing Callable (returns a value). 4) Using ExecutorService thread pool. Thread states: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED.", "MEDIUM");
        addQ(java, "What is the difference between ArrayList and LinkedList?",
             "ArrayList uses a dynamic array — O(1) random access, O(n) insert/delete in middle. LinkedList uses doubly-linked nodes — O(n) random access, O(1) insert/delete at ends. Use ArrayList for frequent reads, LinkedList for frequent insertions/deletions at ends.", "MEDIUM");
        addQ(java, "Explain Java 8 features: Streams, Lambda, Optional.",
             "Lambda: anonymous function syntax (x -> x * 2). Streams: pipeline API for processing collections (filter, map, reduce, collect). Optional: container to handle null values without NPE. Also: default/static methods in interfaces, new Date/Time API (LocalDate, LocalDateTime), method references.", "MEDIUM");
        addQ(java, "What is the Java Memory Model? Explain Heap and Stack.",
             "Stack: stores method calls, local variables, references — thread-specific, LIFO, fast. Heap: stores all objects and arrays — shared among threads, managed by GC. Method Area (Metaspace in Java 8+): stores class definitions, static variables. Each thread has its own stack but shares the heap.", "MEDIUM");
        addQ(java, "What is Garbage Collection? Explain how it works.",
             "GC automatically reclaims memory from unreachable objects. Generations: Young (Eden + Survivor spaces), Old (Tenured), Metaspace. Minor GC collects Young gen, Major/Full GC collects Old gen. Algorithms: Serial, Parallel, G1 (Garbage-First), ZGC. GC roots include stack references, static variables.", "HARD");
        addQ(java, "What are Design Patterns? Explain Singleton and Factory.",
             "Design patterns are reusable solutions to common problems. Singleton: only one instance, private constructor, static getInstance(). Factory: creates objects without specifying exact class, delegates instantiation to subclasses. Other patterns: Observer, Strategy, Decorator, Builder, Proxy.", "HARD");
        addQ(java, "Explain Java generics and bounded type parameters.",
             "Generics provide type safety at compile time. Syntax: List<T>, <T extends Number>, <? super Integer>. Bounded wildcards: upper bounded (? extends T), lower bounded (? super T). Generic methods: <T> T method(T arg). Erasure: generics are removed at runtime (type erasure).", "HARD");
        addQ(java, "What is reflection in Java? When should you use it?",
             "Reflection API allows inspecting and modifying classes, methods, fields at runtime. Classes: Class.forName(), getMethod(), getField(), newInstance(). Use cases: frameworks (Spring, Hibernate), ORMs, testing frameworks, IDEs. Drawbacks: slow, breaks encapsulation, not type-safe.", "HARD");

        // ── DSA ───────────────────────────────────────────────────────────────
        Domain dsa = domainMap.get("DSA");
        addQ(dsa, "What is a Stack? Explain its operations and use cases.",
             "Stack is a LIFO (Last In First Out) data structure. Operations: push() O(1), pop() O(1), peek() O(1), isEmpty() O(1). Implementations: array, linked list. Use cases: function call stack, expression evaluation, undo/redo, browser history, DFS traversal.", "EASY");
        addQ(dsa, "What is a Queue? Explain BFS traversal.",
             "Queue is a FIFO (First In First Out) data structure. Operations: enqueue() O(1), dequeue() O(1). Types: simple, circular, deque, priority queue. BFS uses a queue to explore nodes level by level — enqueue root, dequeue and process, enqueue children. Time: O(V+E), Space: O(V).", "EASY");
        addQ(dsa, "What is the time complexity of Binary Search? Explain the algorithm.",
             "Binary Search: O(log n) time, O(1) space (iterative). Works on sorted arrays. Algorithm: compare mid element with target, if equal return mid, if target < mid search left half, else search right half. Recursive version: O(log n) space due to call stack.", "EASY");
        addQ(dsa, "Explain Bubble Sort with its time and space complexity.",
             "Bubble Sort repeatedly swaps adjacent elements if out of order. Best case: O(n) with optimization flag when already sorted. Worst/Average: O(n²). Space: O(1) in-place. Stable sort. Not used in practice; better for educational purposes and very small arrays.", "EASY");
        addQ(dsa, "What is a Binary Search Tree (BST)? Explain operations.",
             "BST: for each node, left subtree < node < right subtree. Operations: insert O(h), search O(h), delete O(h) where h is height. Balanced BST (AVL, Red-Black): O(log n) guaranteed. Inorder traversal of BST gives sorted output. Degenerate BST becomes O(n) like a linked list.", "MEDIUM");
        addQ(dsa, "Explain Merge Sort with its time and space complexity.",
             "Merge Sort uses divide and conquer: split array in half recursively until single elements, then merge sorted halves. Time: O(n log n) always. Space: O(n) auxiliary. Stable sort. Preferred for linked lists and external sorting. Not in-place unlike Quick Sort.", "MEDIUM");
        addQ(dsa, "What is Dynamic Programming? Give a classic example.",
             "DP solves problems by breaking into overlapping subproblems and storing solutions (memoization/tabulation). Properties: optimal substructure + overlapping subproblems. Example: Fibonacci — naive O(2^n), DP O(n). Other examples: Longest Common Subsequence, Knapsack, Coin Change, Edit Distance.", "MEDIUM");
        addQ(dsa, "Explain Graph BFS and DFS with their complexities.",
             "BFS: uses queue, explores level by level, finds shortest path in unweighted graphs, O(V+E) time and space. DFS: uses stack/recursion, explores as deep as possible, O(V+E) time and O(V) space. BFS: good for shortest paths. DFS: good for topological sort, cycle detection, connected components.", "MEDIUM");
        addQ(dsa, "What is a Heap? Explain heapify and Heap Sort.",
             "Heap is a complete binary tree. Min-Heap: parent ≤ children (root is minimum). Max-Heap: parent ≥ children. Operations: insert O(log n), extract-min/max O(log n), build-heap O(n). Heapify: restore heap property by sifting down. Heap Sort: build max-heap then extract-max n times — O(n log n) in-place, not stable.", "MEDIUM");
        addQ(dsa, "What is Quick Sort? Explain partitioning and its complexity.",
             "Quick Sort picks a pivot and partitions array: elements < pivot go left, > pivot go right, then recursively sort partitions. Lomuto/Hoare partition schemes. Best/Average: O(n log n). Worst: O(n²) with bad pivot (e.g., sorted array with first-element pivot). Space: O(log n) average. In-place, not stable.", "MEDIUM");
        addQ(dsa, "Explain Dijkstra's algorithm for shortest path.",
             "Dijkstra finds shortest path from source to all vertices in weighted graph with non-negative edges. Uses min-priority queue (or heap). Initialize distances to infinity except source = 0. Process vertex with minimum distance, relax neighbors. Time: O((V+E) log V) with binary heap. Doesn't work with negative edges.", "HARD");
        addQ(dsa, "What is a Trie data structure? When would you use it?",
             "Trie (prefix tree): each node represents a character, paths from root represent strings. Operations: insert O(L), search O(L), delete O(L) where L is string length. Use cases: autocomplete, spell checker, IP routing, dictionary implementations. Space efficient for strings with common prefixes.", "HARD");

        // ── DBMS ──────────────────────────────────────────────────────────────
        Domain dbms = domainMap.get("DBMS");
        addQ(dbms, "What is DBMS? What are its advantages over file systems?",
             "DBMS is software for creating, managing, and querying databases. Advantages: data independence, reduced redundancy, data integrity, concurrent access, security, backup/recovery, data abstraction. File systems lack these features: inconsistency, redundancy, difficulty in querying.", "EASY");
        addQ(dbms, "What is the difference between DDL, DML, DCL, and TCL?",
             "DDL (Data Definition Language): CREATE, ALTER, DROP, TRUNCATE — defines schema. DML (Data Manipulation Language): SELECT, INSERT, UPDATE, DELETE — manipulates data. DCL (Data Control Language): GRANT, REVOKE — controls permissions. TCL (Transaction Control Language): COMMIT, ROLLBACK, SAVEPOINT — manages transactions.", "EASY");
        addQ(dbms, "What are Primary Key, Foreign Key, and Unique Key?",
             "Primary Key: uniquely identifies each row, NOT NULL, only one per table. Foreign Key: references primary key of another table, enforces referential integrity. Unique Key: ensures uniqueness but allows one NULL. Candidate Key: any column that can be primary key. Composite Key: primary key using multiple columns.", "EASY");
        addQ(dbms, "What is normalization? Explain 1NF, 2NF, 3NF, and BCNF.",
             "Normalization reduces redundancy and improves data integrity. 1NF: atomic values, no repeating groups. 2NF: 1NF + no partial dependency (non-key attributes depend on entire primary key). 3NF: 2NF + no transitive dependency. BCNF: every determinant is a candidate key. Higher NFs trade space for fewer anomalies.", "MEDIUM");
        addQ(dbms, "What is a JOIN? Explain INNER, LEFT, RIGHT, FULL OUTER JOIN.",
             "JOIN combines rows from multiple tables. INNER JOIN: only matching rows from both tables. LEFT JOIN: all rows from left + matching from right (NULL for no match). RIGHT JOIN: all from right + matching from left. FULL OUTER JOIN: all rows from both tables (NULL where no match). CROSS JOIN: Cartesian product.", "MEDIUM");
        addQ(dbms, "What is an index in SQL? How does it improve performance?",
             "Index is a data structure (B-tree or hash) that speeds up SELECT queries by reducing full table scans. Trade-off: faster reads but slower writes (index must be updated on INSERT/UPDATE/DELETE) and more storage. Types: clustered (physical order), non-clustered, composite, unique, full-text.", "MEDIUM");
        addQ(dbms, "What are ACID properties? Explain each.",
             "ACID guarantees reliable database transactions. Atomicity: transaction is all-or-nothing. Consistency: DB moves from one valid state to another. Isolation: concurrent transactions don't interfere. Durability: committed changes survive failures. Isolation levels: READ UNCOMMITTED, READ COMMITTED, REPEATABLE READ, SERIALIZABLE.", "MEDIUM");
        addQ(dbms, "What is a transaction? Explain isolation levels and their anomalies.",
             "Transaction: sequence of operations treated as a unit. Isolation levels: READ UNCOMMITTED (dirty reads), READ COMMITTED (prevents dirty reads), REPEATABLE READ (prevents non-repeatable reads), SERIALIZABLE (prevents phantom reads). Trade-off: higher isolation = fewer anomalies but lower concurrency.", "MEDIUM");
        addQ(dbms, "What is denormalization? When should you use it?",
             "Denormalization intentionally adds redundancy to improve read performance by reducing JOINs. Used in: read-heavy workloads, reporting/analytics, data warehouses, caching layer. Trade-offs: faster reads, slower writes, risk of data inconsistency, more storage. Use with careful consideration of consistency requirements.", "MEDIUM");
        addQ(dbms, "Explain stored procedures vs functions in SQL.",
             "Stored procedure: precompiled SQL stored in DB, can have IN/OUT params, can use DML/DDL, called with EXEC/CALL. Function: returns a value, used in SELECT, no side effects (pure functions), deterministic. Procedures better for complex business logic; functions for computed columns and reuse in queries.", "HARD");
        addQ(dbms, "What is query optimization? Explain the role of execution plan.",
             "Query optimizer chooses the most efficient execution plan. Execution plan shows: table scan vs index scan, join algorithms (nested loop, hash join, merge join), operation costs. Use EXPLAIN/EXPLAIN PLAN to view. Optimization strategies: add indexes, rewrite queries, avoid SELECT *, use query hints.", "HARD");
        addQ(dbms, "What is sharding? How does it differ from replication?",
             "Sharding: horizontally partitions data across multiple DB servers (each shard holds a subset). Improves write scalability. Sharding strategies: range, hash, geo. Replication: copies same data to multiple servers for high availability and read scalability. Replication lag is a challenge. Sharding adds complexity in queries joining across shards.", "HARD");

        // ── Operating Systems ─────────────────────────────────────────────────
        Domain os = domainMap.get("Operating Systems");
        addQ(os, "What is an Operating System? What are its main functions?",
             "OS manages hardware and software resources and provides services to programs. Functions: process management, memory management, file system management, device management, security/protection, user interface. Types: batch, time-sharing, real-time, distributed, embedded.", "EASY");
        addQ(os, "What is the difference between a process and a thread?",
             "Process: independent program in execution with own memory space (PCB, virtual address space). Thread: lightweight unit within a process, shares process memory. Threads are faster to create/switch. Context switch between processes is expensive. Threads share heap but have own stack. Inter-process communication is harder than inter-thread.", "EASY");
        addQ(os, "What is context switching? Why is it expensive?",
             "Context switching: saving state (registers, PC, stack) of current process/thread and loading state of next. Expensive because: CPU cache must be flushed, memory mappings change, TLB flushed. Overhead: time taken for switching is 'wasted'. Threads have lower context switch cost than processes.", "EASY");
        addQ(os, "What is deadlock? What are the four necessary conditions?",
             "Deadlock: processes wait for each other forever, no progress. Four conditions (Coffman): Mutual Exclusion (only one process uses resource), Hold and Wait (holding one resource while waiting), No Preemption (resources can't be forcibly taken), Circular Wait (circular chain of waiting). All four must hold simultaneously.", "MEDIUM");
        addQ(os, "Explain CPU scheduling algorithms: FCFS, SJF, and Round Robin.",
             "FCFS: simple, non-preemptive, high average waiting time, convoy effect. SJF: optimal average waiting time, requires knowing burst time in advance, starvation possible. Round Robin: preemptive, each process gets time quantum, fair, good for interactive systems, higher context switches. Priority scheduling: may cause starvation — aging prevents it.", "MEDIUM");
        addQ(os, "What is virtual memory? Explain paging and segmentation.",
             "Virtual memory creates illusion of large memory using disk. Paging: divides memory into fixed-size pages and frames, page table maps virtual to physical addresses. Page fault: page not in memory, OS loads from disk. Segmentation: variable-size logical segments. TLB caches recent page table entries for fast lookup.", "MEDIUM");
        addQ(os, "What is the difference between multiprogramming and multitasking?",
             "Multiprogramming: multiple programs loaded in memory simultaneously, CPU switches when one does I/O (improves CPU utilization). Multitasking: rapid context switching between programs giving illusion of parallelism (time-sharing). Multiprocessing: multiple CPUs run processes truly in parallel. Multithreading: multiple threads within one process.", "MEDIUM");
        addQ(os, "Explain semaphores and mutexes. How do they prevent race conditions?",
             "Mutex: binary lock, only owner can release, used for mutual exclusion. Semaphore: integer counter, wait() (P) decrements, signal() (V) increments, allows multiple concurrent access. Binary semaphore ≈ mutex. Counting semaphore: controls access to N resources. Both prevent race conditions by ensuring atomic access to critical sections.", "MEDIUM");
        addQ(os, "What is a page fault? How is it handled?",
             "Page fault: accessed page not in physical memory (invalid page table entry). Handling: trap to OS, find page on disk, find free frame (evict if needed using replacement algorithm — LRU, FIFO, Optimal), load page, update page table, restart instruction. Major fault: page on disk. Minor fault: page in memory but not mapped.", "HARD");
        addQ(os, "Explain the Producer-Consumer problem and its solutions.",
             "Producer-Consumer: producer creates items, consumer consumes, shared bounded buffer. Problems: producer shouldn't add to full buffer, consumer shouldn't remove from empty buffer. Solutions: semaphores (mutex + full + empty counters), monitors with condition variables, blocking queues. Java: BlockingQueue, wait/notify.", "HARD");
        addQ(os, "What is Banker's Algorithm? How does it prevent deadlock?",
             "Banker's Algorithm (Dijkstra): deadlock avoidance algorithm. Maintains Max, Allocation, Need, Available matrices. Before granting resource, checks if system stays in 'safe state' (safe sequence of processes that can complete). If safe, grant; else, process must wait. Requires knowing maximum resource needs in advance.", "HARD");
        addQ(os, "What is disk scheduling? Explain SCAN and C-SCAN algorithms.",
             "Disk scheduling: order of disk I/O requests to minimize seek time. FCFS: simple but poor performance. SSTF: nearest request first, starvation possible. SCAN (elevator): moves in one direction, services requests, then reverses. C-SCAN (circular): only services in one direction, then jumps back to start. C-LOOK: variant that only goes as far as last request.", "HARD");

        // ── Computer Networks ─────────────────────────────────────────────────
        Domain cn = domainMap.get("Computer Networks");
        addQ(cn, "What is the OSI model? Name and describe all 7 layers.",
             "Physical: bits on wire (cables, signals). Data Link: frames, MAC addresses, error detection (Ethernet). Network: packets, IP addressing, routing (IP). Transport: segments, end-to-end delivery, ports (TCP/UDP). Session: establishes, manages, terminates connections. Presentation: encryption, compression, format conversion. Application: user-facing protocols (HTTP, FTP, DNS).", "EASY");
        addQ(cn, "What is the difference between TCP and UDP?",
             "TCP: connection-oriented (3-way handshake), reliable, ordered, error-checked, flow/congestion control, slower. Used for HTTP, FTP, email. UDP: connectionless, unreliable, no ordering guarantee, lower overhead, faster. Used for DNS, video streaming, gaming, VoIP. TCP guarantees delivery; UDP prioritizes speed.", "EASY");
        addQ(cn, "What is an IP address? What is the difference between IPv4 and IPv6?",
             "IP address uniquely identifies a device on a network. IPv4: 32-bit, 4 octets (192.168.1.1), ~4.3 billion addresses, NAT needed. IPv6: 128-bit, hexadecimal (2001:0db8::1), 340 undecillion addresses, built-in security (IPSec), no NAT needed, auto-configuration. IPv4 is still dominant but IPv6 adoption growing.", "EASY");
        addQ(cn, "What is DNS? How does DNS resolution work?",
             "DNS (Domain Name System) translates domain names to IP addresses. Resolution: browser checks cache → OS checks cache → query Recursive Resolver → Root Server (returns TLD server) → TLD Server (.com) → Authoritative DNS Server → returns IP. DNS record types: A (IPv4), AAAA (IPv6), CNAME (alias), MX (mail), NS (nameserver).", "EASY");
        addQ(cn, "What is HTTP? Explain the main HTTP methods and status codes.",
             "HTTP (Hypertext Transfer Protocol) is stateless application-layer protocol for web. Methods: GET (retrieve), POST (create), PUT (update/replace), PATCH (partial update), DELETE (remove), HEAD (headers only). Status codes: 2xx success, 3xx redirect, 4xx client error (404 Not Found, 403 Forbidden), 5xx server error.", "MEDIUM");
        addQ(cn, "What is a subnet mask? Explain subnetting and CIDR.",
             "Subnet mask divides IP into network and host portions. CIDR notation: 192.168.1.0/24 means 24 bits for network. Subnetting: divide a network into smaller subnets. /24 = 256 addresses (254 usable). /25 = 2 subnets of 128 each. Useful for IP management, security isolation, and reducing broadcast domains.", "MEDIUM");
        addQ(cn, "What is HTTPS? Explain the TLS/SSL handshake process.",
             "HTTPS = HTTP + TLS/SSL encryption. TLS Handshake: ClientHello (supported ciphers) → ServerHello (chosen cipher, certificate) → Client verifies certificate with CA → Key exchange (RSA or ECDHE) → Both derive session keys → Encrypted communication begins. Provides confidentiality, integrity, and authentication.", "MEDIUM");
        addQ(cn, "What is a firewall? What are the different types?",
             "Firewall filters network traffic based on rules. Types: Packet filtering (stateless, checks IP/port headers), Stateful inspection (tracks connection state), Application layer (deep packet inspection, understands protocols), Next-gen firewall (IPS, application awareness, user identity). WAF specifically protects web applications.", "MEDIUM");
        addQ(cn, "What is NAT? Why is it used?",
             "NAT (Network Address Translation) maps private IP addresses to a public IP. Types: Static NAT (1-to-1), Dynamic NAT (pool), PAT/Masquerade (many-to-one using ports). Benefits: conserves IPv4 addresses, provides basic security (hides internal topology). Drawback: breaks end-to-end connectivity, complicates some protocols.", "MEDIUM");
        addQ(cn, "What is the difference between routers and switches?",
             "Switch: Layer 2, uses MAC addresses to forward frames within a LAN, maintains MAC address table, reduces collisions (vs hub). Router: Layer 3, uses IP addresses to route packets between networks, maintains routing table, connects different networks (LAN to WAN). Layer 3 switch combines both functionalities.", "MEDIUM");
        addQ(cn, "Explain the TCP three-way handshake and four-way termination.",
             "3-way handshake (connection): SYN (client) → SYN-ACK (server) → ACK (client). Now connection established. 4-way termination: FIN (initiator) → ACK (receiver) → FIN (receiver) → ACK (initiator). TIME_WAIT state ensures all packets in transit are processed. SYN flood attack exploits the 3-way handshake.", "HARD");
        addQ(cn, "What is BGP? How does internet routing work at a high level?",
             "BGP (Border Gateway Protocol) is the routing protocol of the internet, exchanging routing info between Autonomous Systems (AS). Path Vector protocol — each AS advertises reachable IP prefixes. Uses AS_PATH to avoid loops. Interior routing (within AS): OSPF, RIP, EIGRP. BGP hijacking is a major security concern.", "HARD");

        // ── Spring Boot ───────────────────────────────────────────────────────
        Domain sb = domainMap.get("Spring Boot");
        addQ(sb, "What is Spring Boot? How does it differ from the Spring Framework?",
             "Spring Boot is an opinionated extension of Spring that provides auto-configuration, embedded servers (Tomcat/Jetty), and starter dependencies to minimize boilerplate. Spring Framework requires manual configuration (XML or Java config). Spring Boot convention-over-configuration approach enables standalone apps with 'java -jar'. Spring Initializr generates projects.", "EASY");
        addQ(sb, "Explain key Spring annotations: @Component, @Service, @Repository, @Controller.",
             "@Component: generic Spring-managed bean. @Service: business logic layer (same as @Component semantically). @Repository: data access layer, enables exception translation. @Controller: handles web requests. @RestController = @Controller + @ResponseBody. Spring scans for these at startup and creates beans. All are stereotype annotations.", "EASY");
        addQ(sb, "What is Dependency Injection? Explain @Autowired and constructor injection.",
             "DI is an IoC pattern where dependencies are provided externally rather than created internally. @Autowired injects beans by type. Constructor injection (preferred): dependencies in constructor — immutable, easier testing. Setter injection: via @Autowired on setter. Field injection: directly on field (not recommended — harder to test).", "EASY");
        addQ(sb, "What is application.properties vs application.yml? What is profile support?",
             "Both configure Spring Boot apps. application.properties: key=value format. application.yml: YAML hierarchical format. Profiles: application-dev.properties, application-prod.properties. Activate with spring.profiles.active=dev or --spring.profiles.active=prod. @Profile annotation on beans. @ConfigurationProperties for type-safe binding.", "EASY");
        addQ(sb, "What is Spring Data JPA? How does it simplify data access?",
             "Spring Data JPA provides repository abstractions over JPA/Hibernate. Extends JpaRepository<Entity, ID> to get CRUD operations for free. Derived query methods: findByNameAndEmail(). @Query for custom JPQL/native SQL. @Transactional for transaction management. Supports pagination (Pageable) and sorting (Sort). Reduces boilerplate DAO code significantly.", "MEDIUM");
        addQ(sb, "What is Spring Security? How do you configure authentication and authorization?",
             "Spring Security provides authentication, authorization, and protection against attacks. SecurityFilterChain configures HTTP security. AuthenticationProvider handles authentication logic. UserDetailsService loads user by username. hasRole(), hasAuthority() for access control. CSRF protection, session management, remember-me built-in. Method-level security with @PreAuthorize.", "MEDIUM");
        addQ(sb, "What is the difference between @Controller and @RestController?",
             "@Controller returns view names (Thymeleaf, JSP templates). @RestController = @Controller + @ResponseBody — returns data serialized to JSON/XML directly. @ResponseBody tells Spring to write return value to HTTP response body using message converters (Jackson for JSON). Use @RestController for REST APIs, @Controller for MVC web apps.", "MEDIUM");
        addQ(sb, "What is @Transactional? How does it work? What are propagation levels?",
             "@Transactional manages database transactions declaratively. Applied to methods or classes — Spring creates a proxy. Propagation: REQUIRED (default, join existing or create), REQUIRES_NEW (always new), MANDATORY, NEVER, SUPPORTS, NOT_SUPPORTED, NESTED. Isolation levels mirror SQL isolation levels. Rollback on RuntimeException by default, can configure with rollbackFor.", "MEDIUM");
        addQ(sb, "What is Spring Boot Actuator? What endpoints does it provide?",
             "Actuator provides production-ready features for monitoring and management. Endpoints: /health (app health), /metrics (JVM, CPU, memory), /info (app info), /env (environment), /loggers (log levels), /beans (Spring beans), /mappings (URL mappings). Expose via management.endpoints.web.exposure.include. Integrate with Prometheus/Grafana.", "MEDIUM");
        addQ(sb, "Explain AOP (Aspect-Oriented Programming) in Spring.",
             "AOP handles cross-cutting concerns (logging, security, transactions) without polluting business logic. Concepts: Aspect (the concern), Advice (when: @Before, @After, @Around, @AfterReturning, @AfterThrowing), JoinPoint (method execution), Pointcut (expression matching JoinPoints). @Transactional is implemented via AOP. Spring uses JDK dynamic proxy or CGLIB.", "HARD");
        addQ(sb, "What is Spring Boot auto-configuration? How does it work internally?",
             "Auto-configuration detects classes on classpath and configures beans automatically. @EnableAutoConfiguration triggers it. spring.factories (or AutoConfiguration.imports in Spring Boot 3) lists all auto-configuration classes. @ConditionalOnClass, @ConditionalOnMissingBean control when configs apply. Override by defining your own bean. spring.autoconfigure.exclude to disable specific configs.", "HARD");
        addQ(sb, "How would you build a REST API with Spring Boot? What are best practices?",
             "Use @RestController, @RequestMapping, @GetMapping/@PostMapping etc. Return ResponseEntity for full HTTP control. Use DTOs to decouple entity from API. Validate input with @Valid/@Validated. Handle exceptions with @ControllerAdvice + @ExceptionHandler. Use proper HTTP status codes. Implement pagination with Pageable. Secure with Spring Security. Document with Swagger/OpenAPI.", "HARD");

        // ── HR ────────────────────────────────────────────────────────────────
        Domain hr = domainMap.get("HR Interview");
        addQ(hr, "Tell me about yourself.",
             "Use the Present-Past-Future structure. Present: current role/education and key skills. Past: relevant experience, projects, achievements. Future: career goals and why this role aligns. Keep to 2-3 minutes. Focus on professional highlights relevant to the job. End with enthusiasm for the opportunity.", "EASY");
        addQ(hr, "What are your greatest strengths?",
             "Choose 2-3 strengths relevant to the role. Use STAR method to back each with an example. Good examples: problem-solving, fast learning, teamwork, attention to detail, communication. Avoid generic answers — be specific. Example: 'I'm a strong problem-solver — in my last project I debugged a critical performance issue that reduced load time by 40%.'", "EASY");
        addQ(hr, "What are your weaknesses? How are you working on them?",
             "Be honest but choose a real weakness that's not critical for the role. Show self-awareness and improvement. Example: 'I sometimes spend too much time perfecting code before moving on. I've been addressing this by setting time boxes and using done criteria.' Avoid cliché answers like 'I work too hard.'", "EASY");
        addQ(hr, "Why do you want to work at this company?",
             "Research the company thoroughly before answering. Mention: mission alignment, specific products/projects you admire, growth opportunities, tech stack, company culture. Show genuine interest, not just 'you're hiring.' Example: 'I admire your commitment to AI-driven solutions, and I believe my Java/Spring Boot background aligns well with your tech stack.'", "EASY");
        addQ(hr, "Where do you see yourself in 5 years?",
             "Show ambition balanced with realism. Align with company's growth paths. Example: 'In 5 years, I see myself as a senior engineer, having contributed to meaningful products and possibly mentoring junior developers. I'm excited to grow with a company that invests in its engineers.' Avoid: 'I want your job' or 'I don't know.'", "EASY");
        addQ(hr, "Describe a time you worked in a team and handled conflict.",
             "Use STAR method: Situation (what was happening), Task (your role), Action (specific steps you took), Result (positive outcome). Example conflict: disagreement on technical approach. Show: active listening, seeking compromise, focusing on goal not ego, bringing in data/evidence. Emphasize what you learned.", "MEDIUM");
        addQ(hr, "How do you handle pressure and tight deadlines?",
             "Show you have a system. Example: 'I prioritize tasks using the Eisenhower Matrix, break large tasks into milestones, and communicate proactively with my team if delays are likely.' Give a specific example where you delivered under pressure. Mention: time management, communication, asking for help when needed, staying calm.", "MEDIUM");
        addQ(hr, "What motivates you?",
             "Be authentic. Common motivators: solving challenging problems, seeing impact of your work, continuous learning, collaboration, building things from scratch. Connect to the role. Example: 'I'm motivated by solving complex technical challenges and seeing my code improve user experience. Contributing to a product used by thousands gives me real satisfaction.'", "MEDIUM");
        addQ(hr, "Describe your greatest professional achievement.",
             "Use STAR method with quantifiable results. Example: 'I led the migration of our legacy monolith to microservices. Situation: system was scaling poorly. Action: designed the architecture, coordinated with 3 teams, implemented incrementally. Result: 70% improvement in response time, 99.9% uptime during migration.'", "MEDIUM");
        addQ(hr, "How do you handle constructive criticism?",
             "Show maturity and growth mindset. Example: 'I welcome feedback as it helps me grow. When I receive criticism, I listen fully without interrupting, ask clarifying questions if needed, thank the person, then reflect and take action. I've learned some of my best coding practices from code reviews.'", "MEDIUM");
        addQ(hr, "Why should we hire you over other candidates?",
             "Summarize your unique value proposition. Combine: relevant skills, specific experience, cultural fit, and enthusiasm. Example: 'I bring strong full-stack Java/Spring Boot skills, 2 real projects in this domain, a proven track record of delivering features under tight deadlines, and genuine passion for this problem space. I'm a fast learner who contributes from day one.'", "MEDIUM");
        addQ(hr, "Do you have any questions for us?",
             "Always ask thoughtful questions — shows engagement. Good questions: 'What does the onboarding process look like?', 'What are the biggest challenges the team is facing?', 'How does the team collaborate on technical decisions?', 'What growth opportunities are available?', 'What does success look like in this role in 6 months?' Avoid salary questions in first round.", "EASY");

        log.info("✅ {} questions seeded across all domains.", questionRepo.count());
    }

    private void addQ(Domain domain, String questionText, String expectedAnswer, String difficulty) {
        Question q = new Question();
        q.setDomain(domain);
        q.setQuestionText(questionText);
        q.setExpectedAnswer(expectedAnswer);
        q.setDifficulty(difficulty);
        questionRepo.save(q);
    }
}
