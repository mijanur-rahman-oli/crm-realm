
-- 1. All HOT leads
SELECT id, name, email, score
FROM leads
WHERE score >= 80
ORDER BY score DESC, name;

-- 2. Number of leads in each priority group.
SELECT p.priority,
       COUNT(l.id) AS lead_count
FROM (SELECT 'HOT' AS priority, 1 AS sort_order
      UNION ALL SELECT 'WARM', 2
      UNION ALL SELECT 'COLD', 3) AS p
LEFT JOIN leads l
       ON p.priority = CASE
                         WHEN l.score >= 80 THEN 'HOT'
                         WHEN l.score >= 50 THEN 'WARM'
                         ELSE 'COLD'
                       END
GROUP BY p.priority, p.sort_order
ORDER BY p.sort_order;
