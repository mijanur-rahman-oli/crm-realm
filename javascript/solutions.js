'use strict';

function firstNonRepeatingChar(str) {
  if (typeof str !== 'string') {
    throw new TypeError('firstNonRepeatingChar expects a string');
  }

  const chars = Array.from(str);
  const counts = new Map();
  for (const ch of chars) {
    const key = ch.toLowerCase();
    counts.set(key, (counts.get(key) || 0) + 1);
  }

  for (const ch of chars) {
    if (counts.get(ch.toLowerCase()) === 1) {
      return ch;
    }
  }
  return null;
}

function findTwoSum(nums, target) {
  if (!Array.isArray(nums)) {
    throw new TypeError('findTwoSum expects an array of integers');
  }

  const seen = new Map(); 
  for (let i = 0; i < nums.length; i++) {
    const complement = target - nums[i];
    if (seen.has(complement)) {
      return [seen.get(complement), i];
    }
    if (!seen.has(nums[i])) {
      seen.set(nums[i], i);
    }
  }
  return null;
}


function countLeadsByStatus(leads, knownStatuses = []) {
  if (!Array.isArray(leads)) {
    throw new TypeError('countLeadsByStatus expects an array of leads');
  }

  const counts = new Map();
  for (const status of knownStatuses) {
    counts.set(status, 0);
  }
  for (const lead of leads) {
    counts.set(lead.status, (counts.get(lead.status) || 0) + 1);
  }
  return Object.fromEntries(counts);
}

module.exports = { firstNonRepeatingChar, findTwoSum, countLeadsByStatus };
