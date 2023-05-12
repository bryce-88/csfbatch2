import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, lastValueFrom, map } from 'rxjs';
import { Photos } from '../model';

@Component({
  selector: 'app-view2',
  templateUrl: './view2.component.html',
  styleUrls: ['./view2.component.css']
})
export class View2Component implements OnInit, OnDestroy {

  params$!: Subscription
  bundleId!: string
  results!: Photos

  constructor(private activatedRoute: ActivatedRoute, private http: HttpClient, private router: Router) {}

  //implement a call to api Get /bundle/<bundleId>
  async ngOnInit(): Promise<void> {
      // let bundleId = localStorage.getItem('bundleId');
      this.params$ = this.activatedRoute.params.subscribe(
         async (params) => {
          this.bundleId = params['bundleId'];
        }
      )
      const params = new HttpParams()
                      .set('bundleId', this.bundleId); 

      let r = await lastValueFrom(this.http.get<Photos>('/bundle', { params }))
      if (r === undefined) {
        this.router.navigate(['/'])
      } else {
        this.results = r;
      }
      
      // this.results$ = lastValueFrom(this.http.get<Photos>('/bundle', { params })
      //                                 .pipe(
      //                                   map((r: any) => {
      //                                     const rName = r['name']
      //                                     const rDate = r['date']
      //                                     const rTitle = r['title']
      //                                     const rComments = r['comments']
      //                                     const rUrl = r['urls'] 
      //                                     return {
      //                                       name: rName,
      //                                       date: rDate,
      //                                       title: rTitle,
      //                                       comments: rComments,
      //                                       urls: rUrl
      //                                     } as Photos
      //                                   } 
      //                                     )
      //                                 )
      //                 )
  }


  ngOnDestroy(): void {
      this.params$.unsubscribe();
  }

}
