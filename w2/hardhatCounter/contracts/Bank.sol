pragma solidity ^0.8.9;
contract Bank {
 
  mapping (address => uint) balances;
 
  event Deposit(address indexed account, uint256 amount);
  event Withdrawal(address indexed account, uint256 amount);

  // 充值
  function deposit() external payable {
    require(msg.value > 0,"Amount must be greater than zero")
    balances[msg.sender] += msg.value
    emit Deposit(msg.sender,msg.value) 
  }

  // 提取
  function withdraw() external {
    uint256 amount = balances[msg.sender];
    require(amount > 0,"Insufficient balance")
    balances[msg.sender] = 0;
    payable(msg.sender).transfer(amount);
    emit Withdrawal(msg.sender, amount);
  }

  // 提取
  function withdraw() external {
    uint256 amount = balances[msg.sender];
    require(amount > 0,"Insufficient balance")
    balances[msg.sender] = 0;
    payable(msg.sender).transfer(amount);
    emit Withdrawal(msg.sender, amount);
  }

  // 查询
  function balance(address account) external view return (uint256)  {
    return balances[account];
  }


}