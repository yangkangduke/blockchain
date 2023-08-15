pragma solidity ^0.8.9;
contract Counter {
 uint public counter;
 address public owner;
 constructor() {
 counter = 0;
 owner = msg.sender;
 }

   modifier onlyOwner() {
        require(msg.sender == owner, "Only the contract owner can call this function.");
        _; // 继续执行被修饰的函数体
    }

 function count() onlyOwner public {
 counter = counter + 1;
 }
  function add(uint x) public {
 counter = counter + x;
 }
}