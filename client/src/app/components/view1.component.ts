import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-view1',
  templateUrl: './view1.component.html',
  styleUrls: ['./view1.component.css']
})
export class View1Component implements OnInit {

  form!: FormGroup
  imageData = ""
  blob!: Blob

  constructor(private fb: FormBuilder, private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
      this.form = this.createForm();
  }

  createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      title: this.fb.control<string>('', [Validators.required]),
      comments: this.fb.control<string>(''),
      archive: this.fb.control('', [Validators.required])
    })
  }

  submit() {
    const formVal = this.form.value;
    this.upload(formVal)
      .then((result)=>{
        // localStorage.setItem('bundleId', result.bundleId)
        this.router.navigate(['/view2', result.bundleId]);
      }).catch(error=> {
        alert("Upload unsuccessful")
        this.router.navigate(['/view1']);
      })
  }

  upload(form: any){
    const formData = new FormData();
    formData.set("name", form['name']);
    formData.set("title", form['title']);
    formData.set("comments", form['comments']);

    this.imageData = form['archive'];
    this.blob = this.dataURItoBlob(this.imageData);
    formData.set("file", this.blob);

    return lastValueFrom(this.http.post<any>("/upload", formData));
  }

  dataURItoBlob(dataURI: String){
    var byteString = atob(dataURI.split(',')[1]);
    let mimeString = dataURI.split(',')[0].split(';')[0];
    var ar = new ArrayBuffer(byteString.length);
    var ai = new Uint8Array(ar);
    for (var i=0; i <byteString.length; i++){
      ai[i] = byteString.charCodeAt(i);
    }
    return new Blob([ar], {type: mimeString});
  }

}
