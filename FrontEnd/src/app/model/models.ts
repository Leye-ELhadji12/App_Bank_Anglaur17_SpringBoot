export interface Customer {
  id: number;
  name: string;
  email: string;
  account: Account[];
}
export interface Account {
  id: string;
  balance: number;
  createdate: Date;
  status: Status;
  customer: Customer;
  operationList: Operation[];
}

export enum Status {
  Active = 'Active',
  Inactive = 'Inactive',
  Closed = 'Closed'
}

export interface Operation {
  id: number;
  operationdate: Date;
  amount: number;
  typeOp: TypeOp;
  account: Account;
  description: string;
}

export enum TypeOp {
  DEBIT = 'DEBIT',
  CREDIT = 'CREDIT'
}

export interface CurrentAccount extends Account {
  overdraft: number;
}

export interface SavingAccount extends Account {
  interestRate: number;
}
