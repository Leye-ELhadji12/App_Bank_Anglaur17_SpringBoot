import {Component, inject} from '@angular/core';
import {CustomerService} from "./customer.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {Customer} from "../model/models";

@Component({
  selector: 'app-client',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './client.component.html',
  styleUrl: './client.component.sass'
})
export class ClientComponent {

  customerService$ = inject(CustomerService);
  customers$ = this.customerService$.customers$;
  searchForm: FormGroup = new FormGroup<any>({
    searchField: new FormControl("")
  });

  constructor() {
    this.searchKeywordForm();
  }

  searchKeywordForm() {
    this.searchForm = new FormGroup({
      searchField: new FormControl("", [Validators.required])
    })
  }

  toggleValidation() {
    const control = this.searchForm.controls['searchField'];
    if (control.validator === Validators.required) {
      control.clearValidators();
    } else {
      control.setValidators(Validators.required);
    }
    control.updateValueAndValidity();
  }

  searchCustomers() {
    this.customerService$.searchCustomers(this.searchForm?.value.searchField);
  }

  deleteCustomer(customer: Customer) {
    this.customerService$.deleteCustomer(customer.id).subscribe({
        next: (response) => {
          this.searchCustomers();
        }, error: err => {
          console.log(err);
      }
    });
  }

  selectedCustomer(customer$: Customer) {

  }
}
