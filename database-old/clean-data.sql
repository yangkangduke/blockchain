update t_user_account set f_balance=0, f_freeze=0;
delete from t_user_account_action_history;
delete from t_user_account_snapshot;
delete from t_user_account_value_history;
delete from t_transaction;
delete from t_order;
delete from t_dividend_summary;
delete from t_funding_rate;