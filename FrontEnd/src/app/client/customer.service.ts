import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Customer} from "../model/models";
import {catchError, of, tap} from "rxjs";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  http = inject(HttpClient);
  private customersUrl = 'http://localhost:8086/';
  customers$ = signal<Customer[]>([]);

  constructor() { }

  subscribe$ = this.http.get<Customer[]>(this.customersUrl+"customers").pipe(
    tap(data => this.customers$.set(data)),
    takeUntilDestroyed(),
    catchError(() => of([] as Customer[]))
  ).subscribe();

  searchCustomers(keyword: String) {
    this.http.get<Customer[]>(this.customersUrl+"customers/search?word="+keyword).pipe(
      tap(data => this.customers$.set(data)),
      catchError(() => of([] as Customer[]))
    ).subscribe();
    return this.customers$;
  }

  saveCustomer(customer: Customer) {
    return this.http.post<Customer>(this.customersUrl+"customers", customer);
  }

  deleteCustomer(id: number) {
    return this.http.delete(this.customersUrl+"customer/"+id);
  }

}
