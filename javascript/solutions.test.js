'use strict';

const test = require('node:test');
const assert = require('node:assert/strict');
const { firstNonRepeatingChar, findTwoSum, countLeadsByStatus } = require('./solutions');

test('firstNonRepeatingChar', async (t) => {
  await t.test('examples from the brief', () => {
    assert.equal(firstNonRepeatingChar('aabbcdd'), 'c');
    assert.equal(firstNonRepeatingChar('swiss'), 'w');
    assert.equal(firstNonRepeatingChar('aabb'), null);
    assert.equal(firstNonRepeatingChar(''), null);
  });

  await t.test('is case-insensitive when counting, returns original case', () => {
    assert.equal(firstNonRepeatingChar('sTreSS'), 'T');
    assert.equal(firstNonRepeatingChar('Aab'), 'b'); // A and a are the same letter
    assert.equal(firstNonRepeatingChar('aAbB'), null);
  });

  await t.test('single character', () => {
    assert.equal(firstNonRepeatingChar('x'), 'x');
  });

  await t.test('spaces and punctuation are counted like any other character', () => {
    assert.equal(firstNonRepeatingChar('aa bb'), ' '); // the single space is the first unique char
    assert.equal(firstNonRepeatingChar('a b a'), 'b'); // two spaces repeat, so 'b' wins
    assert.equal(firstNonRepeatingChar('aa!bb'), '!');
    assert.equal(firstNonRepeatingChar('a a'), ' '); // 'a' repeats, the lone space does not
    assert.equal(firstNonRepeatingChar('a  a'), null); // two spaces repeat too
  });

  await t.test('handles emoji without splitting them', () => {
    assert.equal(firstNonRepeatingChar('😀😀🙂'), '🙂');
  });

  await t.test('rejects non-strings', () => {
    assert.throws(() => firstNonRepeatingChar(null), TypeError);
    assert.throws(() => firstNonRepeatingChar(123), TypeError);
  });
});

test('findTwoSum', async (t) => {
  const sumsTo = (nums, pair, target) =>
    pair[0] !== pair[1] && nums[pair[0]] + nums[pair[1]] === target;

  await t.test('examples from the brief', () => {
    assert.deepEqual(findTwoSum([2, 7, 11, 15], 9), [0, 1]);
    assert.deepEqual(findTwoSum([3, 2, 4], 6), [1, 2]);
    assert.deepEqual(findTwoSum([3, 3], 6), [0, 1]);
    assert.equal(findTwoSum([1, 2, 3], 10), null);
  });

  await t.test('does not use the same element twice', () => {
    assert.equal(findTwoSum([3], 6), null);
    assert.equal(findTwoSum([3, 4], 6), null);
  });

  await t.test('empty array returns null', () => {
    assert.equal(findTwoSum([], 0), null);
  });

  await t.test('negative numbers and zero', () => {
    const nums = [-3, 4, 3, 90];
    assert.ok(sumsTo(nums, findTwoSum(nums, 0), 0));
    const zeros = [0, 4, 0];
    assert.deepEqual(findTwoSum(zeros, 0), [0, 2]);
  });

  await t.test('any valid pair is accepted when several exist', () => {
    const nums = [1, 4, 5, 6, 2];
    const pair = findTwoSum(nums, 7);
    assert.ok(pair && sumsTo(nums, pair, 7));
  });

  await t.test('rejects non-arrays', () => {
    assert.throws(() => findTwoSum('abc', 3), TypeError);
  });
});

test('countLeadsByStatus', async (t) => {
  await t.test('example from the brief', () => {
    const leads = [
      { name: 'Alice', status: 'NEW' },
      { name: 'Bob', status: 'CONTACTED' },
      { name: 'Charlie', status: 'NEW' },
      { name: 'David', status: 'QUALIFIED' },
    ];
    assert.deepEqual(countLeadsByStatus(leads), { NEW: 2, CONTACTED: 1, QUALIFIED: 1 });
  });

  await t.test('empty input returns an empty object', () => {
    assert.deepEqual(countLeadsByStatus([]), {});
  });

  await t.test('does not mutate the original array', () => {
    const leads = [
      { name: 'Alice', status: 'NEW' },
      { name: 'Bob', status: 'LOST' },
    ];
    const snapshot = JSON.parse(JSON.stringify(leads));
    countLeadsByStatus(leads);
    assert.deepEqual(leads, snapshot);
  });

  await t.test('counts a brand-new status value', () => {
    const leads = [{ name: 'Eve', status: 'ON_HOLD' }];
    assert.deepEqual(countLeadsByStatus(leads), { ON_HOLD: 1 });
  });

  await t.test('optional known statuses are included with a zero count', () => {
    const leads = [{ name: 'Alice', status: 'NEW' }];
    assert.deepEqual(countLeadsByStatus(leads, ['NEW', 'LOST']), { NEW: 1, LOST: 0 });
  });

  await t.test('unusual status strings are safe', () => {
    const result = countLeadsByStatus([{ name: 'X', status: '__proto__' }]);
    assert.equal(Object.getOwnPropertyDescriptor(result, '__proto__').value, 1);
  });
});
